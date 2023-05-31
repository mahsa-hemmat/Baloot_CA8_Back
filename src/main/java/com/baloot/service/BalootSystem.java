package com.baloot.service;

import com.baloot.exception.*;
import com.baloot.info.CommentInfo;
import com.baloot.model.*;
import com.baloot.model.id.BuyListId;
import com.baloot.model.id.RatingId;
import com.baloot.model.id.UserDiscountId;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.Query;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.crypto.spec.SecretKeySpec;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.*;
import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.*;

import static com.baloot.service.JWTService.getHash;

@Component
public class BalootSystem {
    private final ProviderService providerService;
    private final CommodityService commodityService;
    private final CategoryService categoryService ;
    private final UserService userService;
    private final DiscountService discountService;
    private final BuyListService buyListService;
    private final HistoryListService historyListService;
    private final CommentService commentService;
    private final RatingService ratingService;
    private String loggedInUser = "";



    @Autowired
    public BalootSystem(ProviderService ps, CommodityService cs, CategoryService cas, UserService us,
                        DiscountService ds, BuyListService buys, HistoryListService hist,
                        CommentService cvs, RatingService rs) {
        this.providerService = ps;
        this.commodityService = cs;
        this.categoryService = cas;
        this.userService = us;
        this.discountService = ds;
        this.buyListService = buys;
        this.historyListService = hist;
        this.commentService = cvs;
        this.ratingService = rs;
    }



    public void voteComment(Long commentId, int vote) throws CommentNotFoundException {
        Comment comment = commentService.findCommentById(commentId);
        if(vote == 1) {
            int like = comment.getLikeCount();
            comment.setLikeCount(++like);
        } else if (vote == -1){
            int dislike = comment.getDislikeCount();
            comment.setDislikeCount(++dislike);
        }
        commentService.saveComment(comment);
    }

    @PersistenceContext
    private EntityManager entityManager;

    public boolean areTablesCreated() {
        Query query = entityManager.createNativeQuery("SELECT 1 FROM baloot.user LIMIT 1");
        return query.getResultList().size() > 0;
    }

    @PostConstruct
    public void readDataFromServer() {
        if(areTablesCreated())
            return;
        try {
            System.out.println("Importing data ...");
            importDiscounts();
            importUsers();
            importProviders();
            importCommodities();
            importComments();
            System.out.println("Done");
        } catch (Exception e) {
            System.out.println(e.getMessage());
        }
    }
    private void importDiscounts() throws Exception{
        String strJsonData = HTTPRequestHandler("http://5.253.25.110:5000/api/discount");
        ObjectMapper mapper = new ObjectMapper();
        List<Discount> discounts = mapper.readValue(strJsonData, new TypeReference<>() {});
        discountService.saveAll(discounts);
    }

    private void importComments() throws Exception {
        String strJsonData = HTTPRequestHandler("http://5.253.25.110:5000/api/comments");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(strJsonData);
        if(jsonNode.isArray()){
            for (JsonNode commentNode : jsonNode) {
                String email = commentNode.get("userEmail").asText();
                int commodity_id = commentNode.get("commodityId").asInt();
                String text = commentNode.get("text").asText();
                DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");
                String d = commentNode.get("date").asText();
                LocalDate date;
                User user = userService.findUserByEmail(email);
                Commodity commodity = commodityService.getCommodityById(commodity_id);
                Comment comment;
                if(!d.isEmpty()) {
                     date = LocalDate.parse(d, DATE_FORMATTER);
                     comment = new Comment();
                     comment.setUser(user);
                     comment.setCommodity(commodity);
                     comment.setText(text);
                     comment.setDate(date);
                }
                else
                    comment = new Comment(user, commodity, text);
                commentService.saveComment(comment);
            }
        }
    }

    private void importCommodities() throws Exception{
        String strJsonData = HTTPRequestHandler("http://5.253.25.110:5000/api/v2/commodities");
        ObjectMapper mapper = new ObjectMapper();
        JsonNode jsonNode = mapper.readTree(strJsonData);
        if(jsonNode.isArray()){
            for (JsonNode commodityNode : jsonNode) {
                int id = commodityNode.get("id").asInt();
                double score = commodityNode.get("rating").asDouble();
                Commodity commodity = new Commodity(id, commodityNode.get("name").asText(),
                        commodityNode.get("price").asInt(), score, commodityNode.get("inStock").asInt(),
                        commodityNode.get("image").asText());
                double rating = commodityNode.get("rating").asDouble();
                commodity.setRating(rating);
                int providerId = commodityNode.get("providerId").asInt();
                commodity.setProvider(providerService.getProviderById(providerId));
                JsonNode categoriesNode = commodityNode.get("categories");
                Set<Category> categories = new HashSet<>();
                if (categoriesNode.isArray()) {
                    for (JsonNode categoryNode : categoriesNode) {
                        Category category = categoryService.getCategoryByType(categoryNode.asText());
                        if (category == null) {
                            category = new Category();
                            category.setType(categoryNode.asText());
                            categoryService.save(category);
                        }
                        categories.add(category);
                    }
                }
                commodity.setCategories(categories);
                addCommodityToProvider(providerId, commodity);
                commodityService.save(commodity);
                ratingService.save(new Rating(userService.findUserById("admin"), commodity, score));
            }
        }
        calculateProvidersRating();
    }
    public void addCommodityToProvider(int providerId, Commodity commodity) {
        try {
            Provider provider = providerService.getProviderById(providerId);
            provider.addNewCommodity(commodity);
            providerService.save(provider);
        } catch (Exception e){
            System.out.println(e.getMessage());
        }
    }
    private void calculateProvidersRating() {
        List<Provider> providers = providerService.findAll();
        for(Provider provider:providers){
            double rating = 0.0;
            List<Commodity> commodities = provider.getCommodities();
            if(!commodities.isEmpty()) {
                double sum = 0;
                for (Commodity commodity : commodities) {
                    sum += commodity.getRating();
                }
                rating = sum / commodities.size();
            }
            System.out.println(rating);
            provider.setRating(rating);
            providerService.save(provider);
        }
    }
    private void calculateProviderRating(int provider_id)  {
        try {
            Provider provider = providerService.getProviderById(provider_id);
            double rating = 0.0;
            List<Commodity> commodities = provider.getCommodities();
            if (!commodities.isEmpty()) {
                double sum = 0;
                for (Commodity commodity : commodities) {
                    sum += commodity.getRating();
                }
                rating = sum / commodities.size();
            }
            System.out.println(rating);
            provider.setRating(rating);
            providerService.save(provider);
        } catch (ProviderNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }

    public void importProviders() throws Exception{
        String strJsonData = HTTPRequestHandler("http://5.253.25.110:5000/api/v2/providers");
        ObjectMapper mapper = new ObjectMapper();
        List<Provider> providers = mapper.readValue(strJsonData, new TypeReference<>() {});
        providerService.saveAll(providers);

    }

    private void importUsers() throws Exception {
        String strJsonData = HTTPRequestHandler("http://5.253.25.110:5000/api/users");
        ObjectMapper mapper = new ObjectMapper();
        List<User> users = mapper.readValue(strJsonData, new TypeReference<>() {});
        for(User user:users){
            user.setDiscounts(discountService.findAll());
            user.setPassword(Arrays.toString(getHash(user.getPassword())));
        }
        User user = new User("admin", Arrays.toString(getHash("admin_1234")), "admin@info.com",
                "2023-05-20","Tehran",0);
        users.add(user);
        userService.saveAll(users);

    }
    public void addUser(User user) {
        user.setPassword(Arrays.toString(getHash(user.getPassword())));
        userService.save(user);
    }
    public boolean userExists(String username){
        return userService.userExists(username);
    }

    private String HTTPRequestHandler(String users_url) throws URISyntaxException, IOException {
        URL url =  new URI(users_url).toURL();
        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        int status = urlConnection.getResponseCode();
        InputStream inputStream = urlConnection.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        String line;
        StringBuilder lines = new StringBuilder();
        while ((line = bufferedReader.readLine()) != null) {
            lines.append(line);
        }
        String strJsonData = lines.toString();
        bufferedReader.close();
        return strJsonData;
    }

    public Boolean hasAnyUserLoggedIn(){
        return !Objects.equals(loggedInUser, "");
    }
    public void logOutUser() throws Exception {
        if(!hasAnyUserLoggedIn())
            throw new InValidInputException("No user has logged in.");
        loggedInUser = "";
    }
    public User getLoggedInUser() throws InValidInputException, UserNotFoundException {
        if (Objects.equals(loggedInUser, ""))
            throw new InValidInputException("You are not logged in. Please login first");
        return userService.findUserById(loggedInUser);
    }
    public void loginInUser(String username, String password) throws UserNotFoundException {
        User user = userService.findUserById(username);
        if (user.getPassword().equals(Arrays.toString(getHash(password)))) {
            loggedInUser = user.getUsername();
        }
    }
    public boolean isUserValid(String name) throws UserNotFoundException {
        return userService.findUserById(name) != null;
    }
    public Provider getProvider(int id) throws ProviderNotFoundException {
        return providerService.getProviderById(id);
    }
    public User getUser(String username) throws UserNotFoundException {
        return userService.findUserById(username);
    }
    public Commodity getCommodity(int id) throws CommodityNotFoundException {
        return commodityService.getCommodityById(id);
    }
    
     public List<Commodity> getCommodities(){
        return commodityService.getCommodities();
    }
//    public List<Commodity> filterByCategory(String category){
//        return repo.f
//    }

    public List<Commodity> filterByName(String name){
        return commodityService.filterByName(name);
    }

    public List<Commodity> filterByProviderName(String name){
        return commodityService.filterByProviderName(name);
    }
    //public void increaseCredit(int credit){db.increaseCredit(credit);}

    @Transactional
    public void addToBuyList(int commodityId) throws UserNotFoundException, CommodityNotFoundException, InValidInputException, OutOfStockException {
        User user = userService.findUserById(loggedInUser);
        Commodity commodity = commodityService.getCommodityById(commodityId);
        if(commodity.getInStock() == 0)
            throw new OutOfStockException(commodityId);
        if(buyListService.getUserBuyListById(user, commodity) != null){
            throw new InValidInputException("Commodity is already added to buy list");
        }
        BuyList buyList = new BuyList(user, commodity);
        buyListService.save(buyList);
        user.getBuyList().add(buyList);
        commodity.getBuyList().add(buyList);
        userService.save(user);
        commodityService.save(commodity);
    }
    public void removeCommodityFromBuyList(int commodityId) throws CommodityNotFoundException, UserNotFoundException {
        BuyListId buyListId = new BuyListId(loggedInUser, commodityId);
        if(!buyListService.existsById(buyListId))
            throw new CommodityNotFoundException(commodityId);
        buyListService.deleteById(buyListId);
        User user = userService.findUserById(loggedInUser);
        List<BuyList> buyLists = user.getBuyList();
        Iterator<BuyList> iterator = buyLists.iterator();
        while (iterator.hasNext()) {
            BuyList currentBuyList = iterator.next();
            if (currentBuyList.getId().equals(buyListId)) {
                iterator.remove();
                break;
            }
        }
        userService.save(user);
    }

    public List<List<Object>> getHistoryList() throws UserNotFoundException {
        User user = userService.findUserById(loggedInUser);
        List<List<Object>> results = historyListService.getUserCommodities(user);
        if(results.isEmpty())
            return null;
        for (List<Object> lo:results) {
            Set<String> categories = commodityService.getCategoriesForCommodity((Integer) lo.get(0));
            lo.add(categories);
        }
        return results;
    }

    public List<List<Object>> getBuyList() throws UserNotFoundException {
        User user = userService.findUserById(loggedInUser);
        List<List<Object>> results = buyListService.getUserCommodities(user);
        if(results.isEmpty())
            return null;
        for (List<Object> lo:results) {
            Set<String> categories = commodityService.getCategoriesForCommodity((Integer) lo.get(0));
            lo.add(categories);
        }
        return results;
    }


    public boolean userExistsByEmail(String email) {
        return userService.userExistsByEmail(email);
    }

    public void addComment(String text, int commodityId) throws UserNotFoundException, CommodityNotFoundException {
        User user = userService.findUserById(loggedInUser);
        Commodity commodity = commodityService.getCommodityById(commodityId);
        Comment comment = new Comment(user, commodity, text);
        commentService.saveComment(comment);
    }

    public List<CommentInfo> getCommodityComments(int commodityId) throws CommodityNotFoundException {
        Commodity commodity = commodityService.getCommodityById(commodityId);
        List<CommentInfo> comments = new ArrayList<>();
        for(Comment comment:commentService.findByCommodity(commodityId)) {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy/MM/dd");
            String formattedDate = comment.getDate().format(formatter);
            comments.add(new CommentInfo(comment.getId(), comment.getUser().getEmail(),
                    comment.getCommodity().getId(), comment.getText(),formattedDate,comment.getLikeCount(),
                    comment.getDislikeCount()));
        }
        return comments;
    }

    public void rateCommodity(Integer commodityId, int score) throws CommodityNotFoundException, UserNotFoundException, ScoreOutOfBoundsException {
        RatingId ratingId = new RatingId(loggedInUser, commodityId);
        Commodity commodity = commodityService.getCommodityById(commodityId);
        User user = userService.findUserById(loggedInUser);
        if(ratingService.findRatingById(ratingId) == null){
            Rating newRating = new Rating(user, commodity, score);
            ratingService.save(newRating);
        } else {
            Rating rating = ratingService.findRatingById(ratingId);
            rating.setScore(score);
            ratingService.save(rating);
        }
        updateCommodityRating(commodityId);
        calculateProviderRating(commodity.getProviderId());
    }

    private void updateCommodityRating(Integer commodityId) throws CommodityNotFoundException {
        Commodity commodity = commodityService.getCommodityById(commodityId);
        List<Integer> ratings = ratingService.calculateRating(commodityId);
        double sum = 0;
        for(Integer rate : ratings){
            sum += rate;
        }
        double rating = sum/ratings.size();
        commodity.setRating(rating);
        commodityService.save(commodity);
    }


    public List<Commodity> recommenderSystem(int commodityId) throws CommodityNotFoundException {
        return commodityService.recommenderSystem(commodityId);
    }

    public void increaseCredit(int credit) throws UserNotFoundException {
        User user = userService.findUserById(loggedInUser);
        int newCredit = user.getCredit() + credit;
        user.setCredit(newCredit);
        userService.save(user);
        //userService.increaseCredit(loggedInUser, credit);

    }
    private String lastDiscountUsed = "";
    public int submitDiscount(String discountCode) throws UserNotFoundException, InvalidDiscountException, DiscountHasExpiredException {
        User user = userService.findUserById(loggedInUser);
        Discount discount = discountService.getDiscountById(discountCode);
        if(discountService.isDiscountUsedByUser(user, discount))
            throw new DiscountHasExpiredException();
        lastDiscountUsed = discountCode;
        List<Integer> results = buyListService.getUserBuyList(user);
        return calculateDiscountCost(results, discount.getDiscount());
    }

    private int calculateDiscountCost(List<Integer> costs, Integer discount) {
        int totalPrice = 0;
        if(costs==null){
            return totalPrice;
        } else {
            for (Integer cost : costs) {
                totalPrice += cost;
            }
        }
        return totalPrice * (100 - discount) / 100;
    }

    public int getTotalCost() throws UserNotFoundException {
        List<Integer> costs = buyListService.getUserBuyList(userService.findUserById(loggedInUser));
        int totalPrice = 0;
        if(costs==null){
            return totalPrice;
        } else {
            for (Integer cost : costs)
                totalPrice += cost;
        }
        return totalPrice;
    }

    public void updateQuantity(User user, int commodityId, int quantity) throws CommodityNotFoundException, OutOfStockException {
        Commodity commodity = commodityService.getCommodityById(commodityId);
        BuyList buyList = buyListService.getUserBuyListById(user, commodity);
        if(quantity > buyListService.findUserBuyListInStock(user, commodity))
            throw new OutOfStockException(commodityId);
        buyList.setInCart(quantity);
        int cost = commodity.getPrice() * quantity;
        buyList.setCost(cost);
        buyListService.save(buyList);
    }

    public void purchase() throws UserNotFoundException, InvalidDiscountException, DiscountHasExpiredException, NotEnoughCreditException {
        User user = userService.findUserById(loggedInUser);
        List<Integer> costs = buyListService.getUserBuyList(user);
        int totalPrice = 0;
        if(costs!=null && lastDiscountUsed.equals("")) {
            totalPrice = getTotalCost();
        }
        else if(!lastDiscountUsed.equals("")){
            Discount discount = discountService.getDiscountById(lastDiscountUsed);
            if(discountService.isDiscountUsedByUser(user, discount))
                throw new DiscountHasExpiredException();
            totalPrice = calculateDiscountCost(costs, discount.getDiscount());
        }
        if(user.getCredit() < totalPrice)
            throw new NotEnoughCreditException();
        int updatedCredit = user.getCredit() - totalPrice;
        user.setCredit(updatedCredit);
        addBuyListToHistoryList(user);
        buyListService.cleanBuyList();
        if(!lastDiscountUsed.equals("")) {
            UserDiscountId userDiscountId = new UserDiscountId(loggedInUser, lastDiscountUsed);
            UserDiscount userDiscount = discountService.findById(userDiscountId);
            userDiscount.setUsed(true);
            discountService.saveUserDiscount(userDiscount);
        }
        lastDiscountUsed = "";

    }

    private void addBuyListToHistoryList(User user) {
        List<BuyList> buyLists = buyListService.findUserBuyListById(user);
        for(BuyList buyList:buyLists){
            Commodity commodity = buyList.getCommodity();
            int inStock = commodity.getInStock() - buyList.getInCart();
            commodity.setInStock(inStock);
            commodityService.save(commodity);
            historyListService.save(new HistoryList(buyList.getUser(), buyList.getCommodity(), buyList.getInCart()));
        }
    }

    public List<Commodity> filterByCategory(String keyword) {
        return commodityService.filterByCategory(keyword);
    }

    public List<Commodity> sortByName() {
        return commodityService.sortByName();
    }

    public List<Commodity> sortByPrice() {
        return commodityService.sortByPrice();
    }

    public List<Commodity> filterByNameSortByName(String keyword) {
        return commodityService.filterByNameSortByName(keyword);
    }





    public List<Commodity> filterByNameSortByPrice(String keyword) {
        return commodityService.filterByNameSortByPrice(keyword);
    }

    public List<Commodity> filterByCategorySortByName(String keyword) {
        return commodityService.filterByCategorySortByName(keyword);
    }

    public List<Commodity> filterByCategorySortByPrice(String keyword) {
        return commodityService.filterByCategorySortByPrice(keyword);
    }

    public List<Commodity> filterByProviderNameSortByName(String keyword) {
        return commodityService.filterByProviderNameSortByName(keyword);
    }

    public List<Commodity> filterByProviderNameSortByPrice(String keyword) {
        return commodityService.filterByProviderNameSortByPrice(keyword);
    }
    public Set<String> getCommodityCategory(int commodityId) {
        return commodityService.getCategoriesForCommodity(commodityId);
    }
}
