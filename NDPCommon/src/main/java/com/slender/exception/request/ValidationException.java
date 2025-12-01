package com.slender.exception.request;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
