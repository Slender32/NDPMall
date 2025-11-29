package com.slender.handler;

import com.slender.config.manager.ResponseWriterManager;
import com.slender.result.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class SecurityExceptionHandler implements AuthenticationEntryPoint {
    private final ResponseWriterManager responseWriterManager;
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception){
        responseWriterManager.write(Response.fail("未知错误"),response);
    }
}

