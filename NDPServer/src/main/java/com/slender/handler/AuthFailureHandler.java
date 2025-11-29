package com.slender.handler;

import com.slender.config.manager.ResponseWriterManager;
import com.slender.exception.LoginException;
import com.slender.result.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthFailureHandler implements AuthenticationFailureHandler {
    private final ResponseWriterManager responseWriterManager;

    @Override
    public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) {
        switch (exception) {
            case LoginException loginException ->
                    responseWriterManager.write(Response.fail(HttpStatus.UNAUTHORIZED.value(), loginException.getMessage()), response);
            default ->
                    responseWriterManager.write(Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), "未知原因导致登陆失败"), response);
        }
    }
}
