package com.baloot.exception;

public class ProviderNotFoundException extends Exception{
    public ProviderNotFoundException(int id) {
        super("No Provider With Id " + id + " is Found");
    }
}
