package com.slender.filter;

import com.slender.config.manager.RequestConfigManager;
import com.slender.exception.request.RequestMethodException;
import com.slender.exception.request.RequestContentException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestTypeFilter extends OncePerRequestFilter{
    private final RequestConfigManager requestConfigManager;
    @Override
    protected void doFilterInternal(HttpServletRequest request,@NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        requestConfigManager
                .findRequestConfig(request.getRequestURI())
                .ifPresent(requestConfig -> {
                    requestConfig.getMethod().ifPresent(method -> {
                        if(!method.name().equals(request.getMethod())) throw new RequestMethodException();});
                    if(request.getContentType() == null && requestConfig.getBodyType().isPresent()) throw new RequestContentException();
                    requestConfig.getBodyType().ifPresent(mediaType -> {
                        if (mediaType != MediaType.ALL) {
                            MediaType requestType = MediaType.parseMediaType(request.getContentType());
                            if (!mediaType.includes(requestType)) {
                                throw new RequestContentException();
                            }
                        }
                    });
                });
        filterChain.doFilter(request,response);
    }
}
