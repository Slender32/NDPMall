package com.slender.config.manager;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderNotFoundException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.HashSet;

public class AuthenticationTokenManager implements AuthenticationManager {
    private final HashSet<AuthenticationProvider> authenticationProviders=new HashSet<>();
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        for (AuthenticationProvider provider : authenticationProviders) {
            if (provider.supports(authentication.getClass())) {
                return provider.authenticate(authentication);
            }
        }
        throw new ProviderNotFoundException("无法找到对应的Provider");
    }

    public AuthenticationTokenManager addProviders(AuthenticationProvider provider){
        this.authenticationProviders.add(provider);
        return this;
    }
}
