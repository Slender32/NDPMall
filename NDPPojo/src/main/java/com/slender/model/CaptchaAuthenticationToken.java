package com.slender.model;

import com.slender.entity.User;
import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

@Getter
public class CaptchaAuthenticationToken extends AbstractAuthenticationToken {
    private final String email;
    private final Integer captcha;
    private final User authenticatedUser;

    public CaptchaAuthenticationToken(String email,Integer captcha) {
        super(null);
        setAuthenticated(false);
        this.email = email;
        this.captcha = captcha;
        this.authenticatedUser =null;
    }

    public CaptchaAuthenticationToken(User authenticatedUser) {
        super(List.of(authenticatedUser.getAuthority()));
        setAuthenticated(true);
        this.authenticatedUser = authenticatedUser;
        this.email = null;
        this.captcha = null;
    }

    @Override
    public String getCredentials() {
        return null;
    }

    @Override
    public User getPrincipal() {
        return this.authenticatedUser;
    }
}
