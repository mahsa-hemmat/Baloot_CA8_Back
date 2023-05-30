package com.baloot.exception;

public class CommodityNotFoundException extends Exception{
    public CommodityNotFoundException(int id) {
        super("No Commodity With Id "+ id +" is Found");
    }
}
