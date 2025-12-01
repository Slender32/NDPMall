package com.slender.handler;

import com.slender.config.manager.ResponseWriterManager;
import com.slender.result.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class AccessRefuseHandler implements AccessDeniedHandler {
    private final ResponseWriterManager responseWriterManager;
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException){
        responseWriterManager.write(Response.fail(HttpStatus.FORBIDDEN.value(),"权限不足"), response);
    }
}
