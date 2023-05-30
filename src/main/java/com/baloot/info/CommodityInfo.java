package com.baloot.info;

import com.baloot.model.Category;
import com.baloot.model.Commodity;

import java.util.List;
import java.util.Locale;
import java.util.Set;

public class CommodityInfo {
    private final int id;
    private final String name;
    private final int providerId;
    private String providerName;
    private final int price;
    private final Set<Category> categories;
    private final double rating;
    private final int inStock;
    private final String image;
    private int ratingsCount;

    public CommodityInfo(Commodity commodity){
        id = commodity.getId();
        name = commodity.getName();
        price = commodity.getPrice();
        providerId = commodity.getProviderId();
        categories = commodity.getCategories();
        rating = commodity.getRating();
        inStock = commodity.getInStock();
        image = commodity.getImage();
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getProviderName() {
        return providerName;
    }

    public int getPrice() {
        return price;
    }

    public Set<Category> getCategories() {
        return categories;
    }

    public double getRating() {
        return rating;
    }

    public int getInStock() {
        return inStock;
    }

    public String getImage() {
        return image;
    }

    public int getRatingsCount() {
        return ratingsCount;
    }

    public int getProviderId() {
        return providerId;
    }

    public void setProviderName(String providerName) {
        this.providerName = providerName;
    }

    public void setRatingsCount(int ratingsCount) {
        this.ratingsCount = ratingsCount;
    }
}
