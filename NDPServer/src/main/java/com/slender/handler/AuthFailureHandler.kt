package com.slender.handler

import com.slender.config.manager.ResponseWriterManager
import com.slender.exception.authentication.captcha.CaptchaMisMatchException
import com.slender.exception.authentication.captcha.CaptchaNotFoundException
import com.slender.exception.authentication.login.EmailNotFoundException
import com.slender.exception.authentication.login.LoginMisMatchException
import com.slender.exception.authentication.login.LoginPersistenceException
import com.slender.exception.category.ValidationException
import com.slender.message.ExceptionMessage
import com.slender.result.Response
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AuthenticationFailureHandler
import org.springframework.stereotype.Component

@Component
class AuthFailureHandler(
        private val responseWriterManager: ResponseWriterManager
) : AuthenticationFailureHandler {
    override fun onAuthenticationFailure(
            request: HttpServletRequest?,
            response: HttpServletResponse,
            exception: AuthenticationException
    ) {
        val responseData = exception.toErrorResponse()
        responseWriterManager.write(responseData, response)
    }

    private fun AuthenticationException.toErrorResponse(): Response<Void> {
        return when (this) {
            is ValidationException, is LoginMisMatchException ->
                Response.fail(HttpStatus.BAD_REQUEST.value(), message ?: ExceptionMessage.LOGIN_ERROR)
            is LoginPersistenceException ->
                Response.fail(HttpStatus.INTERNAL_SERVER_ERROR.value(), ExceptionMessage.HAS_LOGIN_ERROR)
            is CaptchaNotFoundException ->
                Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.CAPTCHA_NOT_FOUND)
            is CaptchaMisMatchException ->
                Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.CAPTCHA_ERROR)
            is EmailNotFoundException ->
                Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.EMAIL_ERROR)
            else -> Response.fail(ExceptionMessage.LOGIN_ERROR)
        }
    }
}