package com.slender.model.token;

import com.slender.entity.User;
import lombok.Getter;
import lombok.ToString;
import org.springframework.security.authentication.AbstractAuthenticationToken;

import java.util.List;

@ToString
public class PasswordAuthenticationToken extends AbstractAuthenticationToken {

    @Getter
    private final String dataBaseColumn;
    private final User authenticatedUser;
    @Getter
    private final String authenticatedValue;
    private final String password;
    public PasswordAuthenticationToken(String authenticatedValue, String dataBaseColumn, String  password) {
        super(null);
        this.setAuthenticated(false);
        this.authenticatedValue = authenticatedValue;
        this.dataBaseColumn = dataBaseColumn;
        this.password = password;
        this.authenticatedUser =null;
    }
    
    public PasswordAuthenticationToken(User authenticatedUser) {
        super(List.of(authenticatedUser.getAuthority()));
        this.setAuthenticated(true);
        this.authenticatedUser = authenticatedUser;
        this.authenticatedValue = null;
        this.dataBaseColumn = null;
        this.password = null;
    }

    @Override
    public String getCredentials() {
        return this.password;
    }

    @Override
    public User getPrincipal() {
        return this.authenticatedUser;
    }

}
