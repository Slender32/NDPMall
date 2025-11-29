package com.slender.handler;

import com.slender.result.Response;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class MvcExceptionHandler {
    @ExceptionHandler
    public Response<Void> handleException(Exception e){
        log.error("捕获异常:{}",e.getMessage(),e);
        return Response.fail(e.getMessage());
    }

}
