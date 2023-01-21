package com.example.springboot.exceptions;

public class NoSuchSubscriptionExists extends RuntimeException {
    private String message;

    public NoSuchSubscriptionExists() {}

    public NoSuchSubscriptionExists(String message) {
        super(message);
        this.message = message;
    }
}
