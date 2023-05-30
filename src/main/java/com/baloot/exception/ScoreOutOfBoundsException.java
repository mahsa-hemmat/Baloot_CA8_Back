package com.baloot.exception;

public class ScoreOutOfBoundsException extends Exception{
    public ScoreOutOfBoundsException(String message) {
        super("Score Range is From 1 to 10.");
    }
}
