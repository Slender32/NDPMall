package com.slender.exception.authentication.captcha;

import org.springframework.security.core.AuthenticationException;

public class FrequentRequestCaptchaException extends AuthenticationException {
    public FrequentRequestCaptchaException(String message) {
        super(message);
    }
}
