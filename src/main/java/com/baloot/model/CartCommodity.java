package com.baloot.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import java.util.*;

public class CartCommodity {
    private int id;
    private String name;
    private int providerId;
    private int price;
    private Set<Category> categories = new HashSet<>();
    private double rating;
    private int inStock;
    private String image;
    private int quantity = 0;

    public CartCommodity(){}
    public CartCommodity(Commodity co){
        id = co.getId();
        name = co.getName();
        providerId = co.getProviderId();
        price = co.getPrice();
        categories = co.getCategories();
        rating = co.getRating();
        inStock = co.getInStock();
        image = co.getImage();
        quantity = 1;
    }

    public CartCommodity(int id, String name, int providerId, int price, Set<Category> categories, double rating
            , int inStock, String image, int quantity) {
        this.id = id;
        this.name = name;
        this.providerId = providerId;
        this.price = price;
        this.categories = categories;
        this.rating = rating;
        this.inStock = inStock;
        this.image = image;
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
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
        return providerId;
    }
    public int getPrice() {
        return price;
    }
    public void updateInStock(){
        inStock--;
    }
    public int getInStock(){
        return inStock;
    }
    public int getId(){
        return id;
    }
    public String getImage(){ return image; }
    public Set<Category> getCategories(){
        return categories;
    }
    public double getRating(){
        return rating;
    }
    public void setRating(double rating){
        this.rating = rating;
    }

}
