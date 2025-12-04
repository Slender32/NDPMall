package com.slender.handler;

import com.slender.config.manager.ResponseWriterManager;
import com.slender.exception.authentication.captcha.CaptchaMisMatchException;
import com.slender.exception.authentication.captcha.CaptchaNotFoundException;
import com.slender.exception.authentication.login.*;
import com.slender.exception.category.ValidationException;
import com.slender.exception.request.RequestContentException;
import com.slender.exception.request.RequestMethodException;
import com.slender.message.ExceptionMessage;
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
        final Response<Void> responseData= switch (exception){
            case TokenNotFoundException _ -> Response.fail(HttpStatus.UNAUTHORIZED.value(), ExceptionMessage.TOKEN_NOT_FOUND);
            case TokenSignatureException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.TOKEN_SIGNATURE_ERROR);
            case TokenExpiredException _ -> Response.fail(HttpStatus.UNAUTHORIZED.value(), ExceptionMessage.TOKEN_EXPIRE_ERROR);
            case BlockException _ -> Response.fail(HttpStatus.FORBIDDEN.value(), ExceptionMessage.BLOCK_ERROR);
            case LoginNotExpiredException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.LOGIN_NOT_EXPIRED_ERROR);
            case LoginExpiredException _ -> Response.fail(HttpStatus.UNAUTHORIZED.value(), ExceptionMessage.LOGIN_EXPIRED_ERROR);
            case RequestMethodException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.REQUEST_METHOD_ERROR);
            case RequestContentException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.REQUEST_BODY_ERROR);
            case ValidationException e -> Response.fail(HttpStatus.BAD_REQUEST.value(), e.getMessage());
            case LoginPersistenceException _ -> Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ExceptionMessage.HAS_LOGIN_ERROR);
            case CaptchaNotFoundException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.CAPTCHA_NOT_FOUND);
            case CaptchaMisMatchException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.CAPTCHA_ERROR);
            case EmailNotFoundException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.EMAIL_ERROR);
            default -> Response.fail(ExceptionMessage.INTERNAL_ERROR);
        };
        responseWriterManager.write(responseData, response);
    }
}
