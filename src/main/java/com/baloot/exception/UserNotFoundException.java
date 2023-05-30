package com.baloot.exception;

public class UserNotFoundException extends Exception{
    public UserNotFoundException(String message) {
        super("No User With Given Username is Found");
    }
}
