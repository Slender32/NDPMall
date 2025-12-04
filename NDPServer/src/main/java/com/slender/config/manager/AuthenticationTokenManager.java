package com.slender.config.manager;

import com.slender.exception.config.ProviderNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;

import java.util.HashSet;

public final class AuthenticationTokenManager implements AuthenticationManager {
    private final HashSet<AuthenticationProvider> authenticationProviders=new HashSet<>();
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        for (AuthenticationProvider provider : authenticationProviders) {
            if (provider.supports(authentication.getClass())) {
                return provider.authenticate(authentication);
            }
        }
        throw new ProviderNotFoundException();
    }

    public AuthenticationTokenManager addProviders(AuthenticationProvider provider){
        this.authenticationProviders.add(provider);
        return this;
    }
}
