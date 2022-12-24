package com.example.springboot.exceptions;

public class TraineeAlreadySubscribedToCourseException extends RuntimeException {
    private String message;

    public TraineeAlreadySubscribedToCourseException() {}

    public TraineeAlreadySubscribedToCourseException(String message) {
        super(message);
        this.message = message;
    }
}
