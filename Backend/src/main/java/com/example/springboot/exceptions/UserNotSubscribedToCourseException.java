package com.example.springboot.exceptions;

public class UserNotSubscribedToCourseException extends RuntimeException {
    private String message;

    public UserNotSubscribedToCourseException() {}

    public UserNotSubscribedToCourseException(String message) {
        super(message);
        this.message = message;
    }
}
