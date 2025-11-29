package com.slender.filter;

import com.slender.config.manager.RequestConfigManager;
import com.slender.config.manager.ResponseWriterManager;
import com.slender.exception.RequestMethodException;
import com.slender.exception.RequestContentException;
import com.slender.exception.RequestNullContentException;
import com.slender.message.FilterMessage;
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
    protected void doFilterInternal(HttpServletRequest request,@NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            requestConfigManager.findRequestConfig(request.getRequestURI())
                    .ifPresent(requestConfig -> {
                        requestConfig.getMethod().ifPresent(method -> {
                            if(!method.name().equals(request.getMethod())) throw new RequestMethodException();});
                        if(request.getContentType() == null && requestConfig.getBodyType().isPresent()) throw new RequestNullContentException();
                        requestConfig.getBodyType().ifPresent(mediaType -> {
                            if(mediaType != MediaType.ALL && !mediaType.toString().equals(request.getContentType())) throw new RequestContentException();});
                    });
        } catch (RequestMethodException _) {
            responseWriterManager.write(Response.fail(HttpStatus.BAD_REQUEST.value(),
                    FilterMessage.REQUEST_METHOD_ERROR),response);
            return;
        } catch (RequestContentException _) {

            responseWriterManager.write(Response.fail(HttpStatus.BAD_REQUEST.value(),
                    FilterMessage.REQUEST_BODY_ERROR),response);
            return;
        } catch (RequestNullContentException _) {
            responseWriterManager.write(Response.fail(HttpStatus.BAD_REQUEST.value(),
                    FilterMessage.REQUEST_BODY_NULL_ERROR),response);
            return;
        }
        filterChain.doFilter(request,response);
    }
}
