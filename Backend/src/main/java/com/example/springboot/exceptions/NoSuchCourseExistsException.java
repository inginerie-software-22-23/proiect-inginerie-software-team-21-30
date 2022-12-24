package com.example.springboot.exceptions;

public class NoSuchCourseExistsException extends RuntimeException {
    private String message;

    public NoSuchCourseExistsException() {}

    public NoSuchCourseExistsException(String message) {
        super(message);
        this.message = message;
    }
}
