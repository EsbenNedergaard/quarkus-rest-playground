package com.madadipouya.quarkus.example.exception;

public class ValidationException extends Exception {

    public ValidationException(String message) {
        super(message);
    }
}
