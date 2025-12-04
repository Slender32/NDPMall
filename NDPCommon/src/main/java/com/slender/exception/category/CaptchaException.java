package com.slender.exception.category;


import org.springframework.security.core.AuthenticationException;

public class CaptchaException extends AuthenticationException {
    public CaptchaException() {
        super(null);
    }
}
