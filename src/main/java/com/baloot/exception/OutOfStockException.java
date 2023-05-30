package com.baloot.exception;

public class OutOfStockException extends Exception{
    public OutOfStockException(int id) {
        super("Commodity With  Id " + id + " is Out Of Stock");
    }
}
