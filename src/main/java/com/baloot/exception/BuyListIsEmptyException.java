package com.baloot.exception;

public class BuyListIsEmptyException extends Exception{
    public BuyListIsEmptyException() {
        super("Buy List is Empty. No Item To Purchase");
    }
}
