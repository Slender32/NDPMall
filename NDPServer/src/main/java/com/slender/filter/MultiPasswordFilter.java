package com.slender.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.slender.config.manager.FilterConfigManager;
import com.slender.config.manager.ValidatorManager;
import com.slender.dto.authentication.LoginByPasswordRequest;
import com.slender.exception.request.RequestContentException;
import com.slender.message.FilterMessage;
import com.slender.model.token.PasswordAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Slf4j
public class MultiPasswordFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ValidatorManager validatorManager;

    public MultiPasswordFilter(FilterConfigManager filterConfigManager) {
        super(filterConfigManager.getFilterURL(MultiPasswordFilter.class));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        try {
            final LoginByPasswordRequest loginByPasswordRequest = objectMapper.readValue(request.getInputStream(), LoginByPasswordRequest.class);
            validatorManager.validate(loginByPasswordRequest);
            final PasswordAuthenticationToken unAuthenticatedToken = new PasswordAuthenticationToken(
                    loginByPasswordRequest.getAuthenticationValue(),
                    loginByPasswordRequest.getType().getDataBaseColumn(),
                    loginByPasswordRequest.getPassword());
            return getAuthenticationManager().authenticate(unAuthenticatedToken);
        }catch (NullPointerException | MismatchedInputException exception ){
            throw new RequestContentException();
        }catch (IOException e){
            log.error(FilterMessage.REQUEST_READ_ERROR,e);
        }
        return null;
    }
}
