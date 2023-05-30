package com.baloot.exception;

public class CommentNotFoundException extends Exception{
    public CommentNotFoundException(String message) {
            super("No Comment With Given Id is Found");
        }
}
