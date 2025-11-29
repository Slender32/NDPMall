package com.slender.provider;

import com.slender.exception.LoginException;
import com.slender.model.PasswordAuthenticationToken;
import com.slender.service.interfase.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import static com.slender.message.FilterMessage.getMessage;

@Slf4j
@Component
@RequiredArgsConstructor
public class PasswordProvider implements AuthenticationProvider {
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        final PasswordAuthenticationToken token = (PasswordAuthenticationToken) authentication;
        return userService.getByDataBaseColumn(token.getDataBaseColumn(), token.getAuthenticatedValue()).map(user -> {
            if (!passwordEncoder.matches(token.getCredentials(), user.getPassword()))
                throw new LoginException(getMessage(token.getDataBaseColumn()));
            return new PasswordAuthenticationToken(user);
        }).orElseThrow(() -> new LoginException(getMessage(token.getDataBaseColumn())));
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return (PasswordAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
