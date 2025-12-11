package com.slender.handler

import com.slender.config.manager.ResponseWriterManager
import com.slender.message.UserMessage
import com.slender.result.Response
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.Authentication
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler
import org.springframework.stereotype.Component

@Component
class SignOutSuccessHandler(
    private val responseWriterManager: ResponseWriterManager
) : LogoutSuccessHandler {
    override fun onLogoutSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authentication: Authentication?
    ){
        responseWriterManager.write(Response.success<Void?>(UserMessage.LOGOUT_SUCCESS), response)
    }
}
