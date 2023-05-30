package com.baloot.exception;

public class InvalidPasswordException extends Exception{
    public InvalidPasswordException() {
        super("Incorrect Password");
    }

}
