package com.example.springboot.exceptions;

public class TraineeNotSubscribedToCourseException extends RuntimeException {
    private String message;

    public TraineeNotSubscribedToCourseException() {}

    public TraineeNotSubscribedToCourseException(String message) {
        super(message);
        this.message = message;
    }
}
