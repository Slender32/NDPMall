package com.slender.exception.authentication.login;

public class TokenMissingException extends RuntimeException {
    public TokenMissingException(String message) {
        super(message);
    }
}
