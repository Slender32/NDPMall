package com.slender.filter

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.databind.exc.MismatchedInputException
import com.slender.config.manager.FilterConfigManager
import com.slender.config.manager.ValidatorManager
import com.slender.dto.authentication.LoginByPasswordRequest
import com.slender.exception.request.RequestContentException
import com.slender.message.ExceptionMessage
import com.slender.model.token.PasswordAuthenticationToken
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.slf4j.LoggerFactory
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.web.authentication.AbstractAuthenticationProcessingFilter
import java.io.IOException

class MultiPasswordFilter(
    filterConfigManager: FilterConfigManager,
    private val objectMapper: ObjectMapper,
    private val validatorManager: ValidatorManager
) : AbstractAuthenticationProcessingFilter(
    filterConfigManager.getFilterURL(MultiPasswordFilter::class.java)
) {
    private val log= LoggerFactory.getLogger(javaClass)
    @Throws(AuthenticationException::class)
    override fun attemptAuthentication(
        request: HttpServletRequest,
        response: HttpServletResponse
    ): Authentication? {
        try {
            val loginByPasswordRequest = objectMapper.readValue(
                request.inputStream,
                LoginByPasswordRequest::class.java
            )
            validatorManager.validate(loginByPasswordRequest)
            val unAuthenticatedToken = PasswordAuthenticationToken(
                loginByPasswordRequest.authenticationValue,
                loginByPasswordRequest.type.dataBaseColumn,
                loginByPasswordRequest.password
            )
            return authenticationManager.authenticate(unAuthenticatedToken)
        } catch (_: NullPointerException) {
            throw RequestContentException()
        } catch (_: MismatchedInputException) {
            throw RequestContentException()
        } catch (e: IOException) {
            log.error(ExceptionMessage.REQUEST_READ_ERROR, e)
        }
        return null
    }
}
