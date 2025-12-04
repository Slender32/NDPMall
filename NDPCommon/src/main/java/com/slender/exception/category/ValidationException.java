package com.slender.exception.category;

import org.springframework.security.core.AuthenticationException;

public class ValidationException extends AuthenticationException {
    public ValidationException(String message) {
        super(message);
    }
}
