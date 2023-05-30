package com.baloot.service;

import com.baloot.model.*;
import com.baloot.exception.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.regex.Pattern;

public class DataBase {
    private static final String USERNAME_PATTERN = "^[a-zA-Z0-9]+$";
    private Map<String, User> users;
    private Map<Integer, Provider> providers;
    private CommodityList commodities;
    private User loggedInUser;

    public DataBase(){
        users = new HashMap<>();
        providers = new HashMap<>();
        commodities = new CommodityList();
        loggedInUser = null;
    }
    private static final Pattern pattern = Pattern.compile(USERNAME_PATTERN);

    public CommodityList getCommodities() {
        return commodities;
    }

    public Provider getProviderById(int id) throws ProviderNotFoundException {
        if(!(providers.containsKey(id)))
            throw new ProviderNotFoundException(id);
        return providers.get(id);
    }
    public void addUser(User userData) throws Exception{
        if (!(pattern.matcher(userData.getUsername()).find()))
            throw new InvalidUserException("InValidUsername: only alphanumeric characters are allowed in username");
        else
            users.put(userData.getUsername(),userData);
    }
    public void addUser(List<User> userData) throws Exception{
        for(User user: userData)
            if (!(pattern.matcher(user.getUsername()).find()))
                throw new InvalidUserException("InValidUsername: only alphanumeric characters are allowed in username");
            else
                users.put(user.getUsername(),user);
    }
    public void addProvider(List<Provider> providerData) throws Exception{
        for(Provider provider:providerData)
            providers.put(provider.getId(), provider);
    }
    public void addCommodity(List<Commodity> commodityData) throws Exception{
        for(Commodity commodity:commodityData)
            if(!(providers.containsKey(commodity.getProviderId())))
                throw new ProviderNotFoundException(commodity.getProviderId());
            else {
                //commodity.setTotalRating(commodity.getRating());
                commodities.addCommodity(commodity);
                providers.get(commodity.getProviderId()).addNewCommodity(commodity);
            }
    }

    public void addToBuyList(int commodityId) throws OutOfStockException, CommodityNotFoundException, UserNotFoundException, InValidInputException {
        /*if(commodities.getCommodityById(commodityId).getInStock() == 0)
            throw new OutOfStockException(commodityId);
        if(!(commodities.hasCommodity(commodityId)))
            throw new CommodityNotFoundException(commodityId);
        if(!(users.containsKey(loggedInUser.getUsername())))
            throw new UserNotFoundException("no user with given username is found");
        users.get(loggedInUser.getUsername()).getBuyList().addCommodity(commodities.getCommodityById(commodityId));*/
    }

    public void rateCommodity(int commodityId, int score) throws ScoreOutOfBoundsException, UserNotFoundException, CommodityNotFoundException {
        /*if( (score<1) || (score > 10))
            throw new ScoreOutOfBoundsException("score range is from 1 to 10.");
        if(!(users.containsKey(loggedInUser.getUsername())))
            throw new UserNotFoundException("no user with given username is found");
        if(!(commodities.hasCommodity(commodityId)))
            throw new CommodityNotFoundException(commodityId);
        boolean newRating=!users.get(loggedInUser.getUsername()).hasRating(commodityId);
        int update=users.get(loggedInUser.getUsername()).getRating(score,commodityId);
        Commodity commodity = commodities.getCommodityById(commodityId);
        commodity.updateRating(update,newRating);
        if(loggedInUser.getBuyList().getCommodities().containsKey(commodityId)) {
            loggedInUser.getBuyList().getCommodities().get(commodityId).setRating(commodity.getRating());
        }
        if(loggedInUser.getPurchasedList().containsKey(commodityId)) {
            loggedInUser.getPurchasedList().get(commodityId).setRating(commodity.getRating());
        }

        providers.get(commodity.getProviderId()).calRating();*/
    }

    public void increaseCredit(int amount){
        //users.get(loggedInUser.getUsername()).increaseCredit(amount);
    }

    public Commodity getCommodityById(int id) throws CommodityNotFoundException {
        if(!(commodities.hasCommodity(id)))
            throw new CommodityNotFoundException(id);
        return commodities.getCommodityById(id);

    }
    public void voteComment(String id, int vote) throws CommentNotFoundException {
        commodities.voteComment(id,vote);
    }

    public void addComment(List<Comment> comments) throws Exception {
        for (Comment comment : comments) {
            //int commodityId = comment.getCommodityId();
            //if (!commodities.hasCommodity(commodityId))
            //    throw new CommodityNotFoundException(commodityId);
            //commodities.getCommodityById(commodityId).addComment(comment);
        }
    }

    public void addComment(String text, int commodityId) throws CommodityNotFoundException {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        String date= formatter.format(new Date());
        if(!commodities.hasCommodity(commodityId))
            throw new CommodityNotFoundException(commodityId);
        //Comment comment = new Comment(loggedInUser.getUsername(), commodityId, text, date);
        //commodities.getCommodityById(commodityId).addComment(comment);
    }

    public String getUserByEmail(String userEmail) throws UserNotFoundException {
        for (User user: users.values())
            if(user.getEmail().equals(userEmail))
                return user.getUsername();
        throw new UserNotFoundException(userEmail);

    }

    public User getUserById(String id) throws Exception{
        if(!users.containsKey(id))
            throw new UserNotFoundException(id);
        return users.get(id);
    }

    public void setLoggedInUser(String username, String password) throws Exception{
        if(!users.containsKey(username))
            throw new UserNotFoundException(username);
        if(!users.get(username).getPassword().equals(password))
            throw new InvalidPasswordException();
        this.loggedInUser = users.get(username);
    }

    public void logOutUser() throws Exception {
        if(!hasAnyUserLoggedIn())
            throw new InValidInputException("No user has logged in.");
        loggedInUser = null;
    }

    public User getLoggedInUser() throws InValidInputException {
        if (loggedInUser == null)
            throw new InValidInputException("You are not logged in. Please login first");
        return loggedInUser;
    }

    public Boolean hasAnyUserLoggedIn() {
        return loggedInUser != null;
    }

    public void purchase() throws OutOfStockException, BuyListIsEmptyException, NotEnoughCreditException, CommodityNotFoundException {
       /* loggedInUser.completePurchase();
        for(CartCommodity commodity:loggedInUser.getBuyList().getCommodities().values()) {
            commodity.updateInStock();
            getCommodityById(commodity.getId()).updateInStock();
        }*/
    }

    public void addDiscount(List<Discount> discounts) {
        /*for(User user: users.values()){
            for (Discount discount : discounts) {
                Discount userDiscount = new Discount(discount);
                user.addDiscount(userDiscount);
            }
        }*/
    }

    public List<Commodity> recommenderSystem(int commodityId) throws CommodityNotFoundException {
        Map<Commodity, Double> scores = new HashMap<>();
        for(Commodity commodity : commodities.getCommodities().values()) {
            if(commodity.getId() == commodityId)
                continue;
            double score = 0;
            if (commodity.getCategories().equals(commodities.getCommodityById(commodityId).getCategories()))
                score = 11 + commodity.getRating();
            else
                score = commodity.getRating();
            scores.put(commodity, score);
        }
        Map<Commodity, Double> sortedScores = new LinkedHashMap<>();
        scores.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .forEachOrdered(x -> sortedScores.put(x.getKey(), x.getValue()));
        return new ArrayList<>(sortedScores.keySet()).subList(0, Math.min(sortedScores.size(), 4));
    }
}
