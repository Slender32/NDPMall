package com.slender.exception.request;

import com.slender.exception.category.RequestException;

public class FrequentRequestCaptchaException extends RequestException {
    public FrequentRequestCaptchaException(String message) {
        super(message);
    }
}
