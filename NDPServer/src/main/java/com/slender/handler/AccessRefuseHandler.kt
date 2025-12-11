package com.slender.handler

import com.slender.config.manager.ResponseWriterManager
import com.slender.message.ExceptionMessage
import com.slender.result.Response
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.security.access.AccessDeniedException
import org.springframework.security.web.access.AccessDeniedHandler
import org.springframework.stereotype.Component

@Component
class AccessRefuseHandler(
    private val responseWriterManager: ResponseWriterManager
) : AccessDeniedHandler {

    override fun handle(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        accessDeniedException: AccessDeniedException?
    ) {
        responseWriterManager.write(
            Response.fail<Void>(
                HttpStatus.FORBIDDEN.value(),
                ExceptionMessage.AUTHORITY_ERROR
            ), response
        )
    }
}
