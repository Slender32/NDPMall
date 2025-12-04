package com.slender.filter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.exc.MismatchedInputException;
import com.slender.config.manager.FilterConfigManager;
import com.slender.config.manager.ValidatorManager;
import com.slender.dto.authentication.LoginByCaptchaRequest;
import com.slender.exception.request.RequestContentException;
import com.slender.message.FilterMessage;
import com.slender.model.token.CaptchaAuthenticationToken;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter;

import java.io.IOException;

@Slf4j
public class CaptchaFilter extends AbstractAuthenticationProcessingFilter {
    @Autowired private ObjectMapper objectMapper;
    @Autowired private ValidatorManager validatorManager;

    public CaptchaFilter(FilterConfigManager filterConfigManager) {
        super(filterConfigManager.getFilterURL(CaptchaFilter.class));
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException{
        try {
            LoginByCaptchaRequest loginByCaptchaRequest = objectMapper.readValue(request.getInputStream(), LoginByCaptchaRequest.class);
            validatorManager.validate(loginByCaptchaRequest);
            return getAuthenticationManager().authenticate(new CaptchaAuthenticationToken(
                    loginByCaptchaRequest.getEmail(), loginByCaptchaRequest.getCaptcha()));
        }catch (NullPointerException | MismatchedInputException _){
            throw new RequestContentException();
        }catch (IOException e) {
            log.info(FilterMessage.REQUEST_READ_ERROR,e);
        }
        return null;
    }
}
