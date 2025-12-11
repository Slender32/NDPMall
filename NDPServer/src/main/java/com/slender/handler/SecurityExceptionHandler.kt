package com.slender.handler

import com.slender.config.manager.ResponseWriterManager
import com.slender.message.ExceptionMessage
import com.slender.result.Response
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.AuthenticationEntryPoint
import org.springframework.stereotype.Component

@Component
class SecurityExceptionHandler(
    private val responseWriterManager: ResponseWriterManager
) : AuthenticationEntryPoint {
    override fun commence(
        request: HttpServletRequest,
        response: HttpServletResponse,
        exception: AuthenticationException?
    ) {
        responseWriterManager.write(
            Response.fail<Void>(
                HttpStatus.UNAUTHORIZED.value(),
                ExceptionMessage.UNKNOWN_ERROR
        ), response)
    }
}

