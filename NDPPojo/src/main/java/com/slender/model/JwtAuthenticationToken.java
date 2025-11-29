package com.slender.model;

import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

public class JwtAuthenticationToken extends AbstractAuthenticationToken {
    private final Long uid;

    public JwtAuthenticationToken(LoginDataCache loginDataCache) {
        super(List.of(loginDataCache.authority()));
        setAuthenticated(true);
        this.uid=loginDataCache.uid();
    }

    public JwtAuthenticationToken(Long uid){
        super(null);
        setAuthenticated(true);
        this.uid=uid;
    }

    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Long getPrincipal() {
        return uid;
    }
}
