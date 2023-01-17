package com.example.springboot.exceptions;

public class NoSuchRecipeExistsException extends RuntimeException {
    private String message;

    public NoSuchRecipeExistsException() {}

    public NoSuchRecipeExistsException(String message) {
        super(message);
        this.message = message;
    }
}
