package com.slender.exception.category;

import org.springframework.security.core.AuthenticationException;

public class RequestException extends AuthenticationException {
    public RequestException() {
        super(null);
    }
    public RequestException(String message) {
        super(message);
    }
}
