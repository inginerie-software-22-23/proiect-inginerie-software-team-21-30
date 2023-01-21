package com.example.springboot.exceptions;

public class UserAlreadySubscribedToCourseException extends RuntimeException {
    private String message;

    public UserAlreadySubscribedToCourseException() {}

    public UserAlreadySubscribedToCourseException(String message) {
        super(message);
        this.message = message;
    }
}
