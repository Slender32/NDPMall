package com.slender.exception.authentication.login;

import com.slender.exception.category.LoginException;

public class LoginMisMatchException extends LoginException {
    public LoginMisMatchException(String message) {
        super(message);
    }
}
