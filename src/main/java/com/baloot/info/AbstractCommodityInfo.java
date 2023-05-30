package com.baloot.info;

import com.baloot.model.Commodity;

public class AbstractCommodityInfo {
    private final int id;
    private final String name;
    private final int price;
    private final int inStock;
    private final String image;

    public AbstractCommodityInfo(Commodity commodity){
        id = commodity.getId();
        name = commodity.getName();
        price = commodity.getPrice();
        inStock = commodity.getInStock();
        image = commodity.getImage();
    }

    public int getId(){
        return id;
    }
    public String getName(){
        return name;
    }

    public int getPrice() {
        return price;
    }

    public int getInStock() {
        return inStock;
    }

    public String getImage(){
        return image;
    }
}
