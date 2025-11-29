package com.slender.handler;

import com.slender.config.manager.ResponseWriterManager;
import com.slender.message.FilterMessage;
import com.slender.result.Response;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignOutSuccessHandler implements LogoutSuccessHandler {
    private final ResponseWriterManager responseWriterManager;
    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        responseWriterManager.write(Response.success(FilterMessage.LOGOUT_SUCCESS), response);
    }
}
