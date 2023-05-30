package com.baloot.model;

import jakarta.persistence.*;
import java.util.*;

@Entity
@Table(name = "commodity")
public class Commodity {
    @Id
    @Column(name = "id")
    private Integer id;

    private String name;
    @ManyToOne
    @JoinColumn(name = "provider_id", nullable = false, referencedColumnName = "id")
    private Provider provider;

    private Integer price;
    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(
            name = "commodity_categories",
            joinColumns = @JoinColumn(name = "commodity_id"),
            inverseJoinColumns = @JoinColumn(name = "category_id")
    )
    private Set<Category> categories = new HashSet<>();
    @Column(name = "rating", columnDefinition = "DECIMAL(10,2) DEFAULT '0.00'")
    private double rating;
    @Column(name = "in_stock")
    private Integer inStock;
    @Column(length = 1024)
    private String image;
    @OneToMany(mappedBy = "commodity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<BuyList> buyLists = new ArrayList<>();
    @OneToMany(mappedBy = "commodity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<HistoryList> historyLists = new ArrayList<>();
    @OneToMany(mappedBy = "commodity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Rating> ratings = new ArrayList<>();
    @OneToMany(mappedBy = "commodity", cascade = CascadeType.REMOVE, orphanRemoval = true)
    private List<Comment> comments = new ArrayList<>();
    @Transient
    private double totalRating = 0;
    @Transient
    private int ratingCount = 1;
    public Commodity(){}
    public Commodity(Integer id, String name, Integer price, double rating, Integer inStock, String image){
        this.id = id;
        this.name = name;
        this.price = price;
        this.rating = rating;
        this.inStock = inStock;
        this.image = image;
    }
    public int getId(){
        return id;
    }
    public void setId(int id) {
        this.id = id;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public int getProviderId() {
        return provider.getId();
    }
    public Provider getProvider() {
        return provider;
    }
    public void setProvider(Provider provider) {
        this.provider = provider;
    }
    public int getPrice() {
        return price;
    }
    public void setPrice(int price) {
        this.price = price;
    }
    public Set<Category> getCategories() {
        return categories;
    }
    public void setCategories(Set<Category> categories) {
        this.categories = categories;
    }
    public double getRating() {
        return rating;
    }
    public void setRating(double rating) {
        this.rating = rating;
    }
    public int getInStock(){
        return inStock;
    }
    public void setInStock(int inStock) {
        this.inStock = inStock;
    }
    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }
    public List<BuyList> getBuyList() {
        return buyLists;
    }
    public void setBuyList(List<BuyList> buyLists) {
        this.buyLists = buyLists;
    }
    public List<HistoryList> getHistoryLists() {
        return historyLists;
    }
    public void setHistoryLists(List<HistoryList> historyLists) {
        this.historyLists = historyLists;
    }
    public List<Comment> getComments() {
        return comments;
    }
    public void setComments(List<Comment> comments) {
        this.comments = comments;
    }

    public List<Rating> getRatings() {
        return ratings;
    }

    public void setRatings(List<Rating> ratings) {
        this.ratings = ratings;
    }

    public void updateInStock(){
        inStock--;
    }
    public void updateRating(double rating,boolean newRating){
        totalRating+=rating;
        if(newRating)
            ratingCount+=1;
        this.rating = totalRating / ratingCount;
    }
    public void setTotalRating(double totalRating) {
        this.totalRating = totalRating;
    }
    public int getRatingCount() {
        return ratingCount;
    }
}
