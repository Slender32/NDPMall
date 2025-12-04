package com.slender.exception.category;

import org.springframework.security.core.AuthenticationException;

public class LoginException extends AuthenticationException {

    public LoginException() {
        super(null);
    }
    public LoginException(String message) {
        super(message);
    }
}
