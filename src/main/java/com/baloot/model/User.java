package com.baloot.model;

import com.baloot.exception.*;
import jakarta.persistence.*;
import java.util.*;


@Entity
@Table(name = "user")
public class User {
    @Id
    @Column(name = "id")
    private String username;
    private String password;
    @Column(unique = true)
    private String email;
    private String birthDate;
    private String address;
    private int credit;
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BuyList> buyList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HistoryList> historyList = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();
    @OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();

    @ManyToMany
    @JoinTable(
            name = "user_discount",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "discount_id")
    )
    private List<Discount> discounts = new ArrayList<>();
    public User(){}
    public User(String username, String password, String email, String birthDate, String address, int credit){
        this.username = username;
        this.password = password;
        this.email = email;
        this.birthDate = birthDate;
        this.address = address;
        this.credit = credit;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getBirthDate() {
        return birthDate;
    }
    public void setBirthDate(String birthDate) {
        this.birthDate = birthDate;
    }
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getCredit() {
        return credit;
    }
    public void setCredit(int credit) {
        this.credit = credit;
    }
    public List<BuyList> getBuyList() {
        return buyList;
    }

    public void setBuyList(List<BuyList> buyList) {
        this.buyList = buyList;
    }

    public List<HistoryList> getHistoryList() {
        return historyList;
    }

    public void setHistoryList(List<HistoryList> historyList) {
        this.historyList = historyList;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public List<Discount> getDiscounts() {
        return discounts;
    }

    public void setDiscounts(List<Discount> discounts) {
        this.discounts = discounts;
    }

    /*public int getRating(int rating, int id){
        int curr_rating=rating;
        if(ratings.containsKey(id)){
            curr_rating=rating-ratings.get(id);
        }
        ratings.put(id,rating);
        return curr_rating;
    }
    public boolean hasRating(int id){
        return ratings.containsKey(id);
    }

    public void increaseCredit(int amount) {
        credit+=amount;
    }


    public void completePurchase() throws NotEnoughCreditException, BuyListIsEmptyException, OutOfStockException {
        int price = 0;
        if (buyList.getDiscount() == 0)
            price = buyList.getTotalCost().get("originalPrice");
        else
            price = buyList.getTotalCost().get("discountedPrice");
        if(credit < price)
            throw new NotEnoughCreditException();
        if(buyList.getCommodities().isEmpty())
            throw new BuyListIsEmptyException();
        //for(CartCommodity commodity:buyList.getCommodities()) {
        for(Commodity commodity:buyList.getCommodities()) {
            if (commodity.getInStock() == 0)
                throw new OutOfStockException(commodity.getId());
        }
        credit -= price;
        purchasedList.putAll(buyList.getCommodities());
        buyList = new BuyList();
        if(lastDiscountUsed != null)
            discounts.get(lastDiscountUsed).setAlreadyUsed(true);
    }

    public void addDiscount(Discount discount){
        discounts.put(discount.getDiscountCode(), discount);
    }
    public Boolean isDiscountValid(String discountCode){return discounts.containsKey(discountCode);}

    public void submitDiscount(String discountCode) throws InvalidDiscountException, DiscountHasExpiredException {
        if (discountCode == null || discountCode.isEmpty() || discountCode.isBlank())
            buyList.setDiscount(0);
        else if(!isDiscountValid(discountCode))
            throw new InvalidDiscountException(discountCode);
        else if(isDiscountValid(discountCode) && !hasDiscountExpired(discountCode)) {
            lastDiscountUsed = discountCode;
            buyList.setDiscount(discounts.get(discountCode).getDiscount());
        }
        else if (isDiscountValid(discountCode) && hasDiscountExpired(discountCode))
            throw new DiscountHasExpiredException();
    }
    public Boolean hasDiscountExpired(String discountCode){
        return discounts.get(discountCode).getAlreadyUsed();
    }*/
}
