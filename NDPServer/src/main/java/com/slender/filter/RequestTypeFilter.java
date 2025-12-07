package com.slender.filter;

import com.slender.config.manager.RequestConfigManager;
import com.slender.config.manager.ResponseWriterManager;
import com.slender.exception.category.RequestException;
import com.slender.exception.request.RequestMethodException;
import com.slender.exception.request.RequestContentException;
import com.slender.message.ExceptionMessage;
import com.slender.result.Response;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class RequestTypeFilter extends OncePerRequestFilter{
    private final RequestConfigManager requestConfigManager;
    private final ResponseWriterManager responseWriterManager;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,@NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            requestConfigManager
                    .findRequestConfig(request.getRequestURI())
                    .ifPresent(requestConfig -> {
                        requestConfig.getMethod().ifPresent(method -> {
                            if(!method.name().equals(request.getMethod())) throw new RequestMethodException();
                        });
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
        } catch (RequestException | NullPointerException exception ) {
            final Response<Void> responseData = switch (exception){
                case RequestMethodException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.REQUEST_METHOD_ERROR);
                case RequestContentException _, NullPointerException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.REQUEST_BODY_ERROR);
                default -> Response.fail(ExceptionMessage.INTERNAL_ERROR);
            };
            responseWriterManager.write(responseData, response);
            return;
        }
        filterChain.doFilter(request,response);
    }
}
