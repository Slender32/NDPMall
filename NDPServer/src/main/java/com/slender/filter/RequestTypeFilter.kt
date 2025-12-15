package com.slender.filter

import com.slender.config.manager.RequestConfigManager
import com.slender.config.manager.ResponseWriterManager
import com.slender.exception.request.RequestContentException
import com.slender.exception.request.RequestMethodException
import com.slender.message.ExceptionMessage
import com.slender.result.Response
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.http.HttpStatus
import org.springframework.http.MediaType
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException
import java.util.function.Consumer

@Component
class RequestTypeFilter(
    private val requestConfigManager: RequestConfigManager,
    private val responseWriterManager: ResponseWriterManager
) : OncePerRequestFilter() {
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        try {
            requestConfigManager
                .findRequestConfig(request.requestURI)
                .ifPresent(Consumer { requestConfig ->
                    requestConfig.getMethod().ifPresent{ method ->
                        if (method.name() != request.method) throw RequestMethodException()
                    }
                    if (request.contentType == null && requestConfig.getBodyType().isPresent) throw RequestContentException()
                    requestConfig.getBodyType().ifPresent{mediaType ->
                        if (mediaType !== MediaType.ALL) {
                            val requestType = MediaType.parseMediaType(request.contentType)
                            if (!mediaType.includes(requestType)) {
                                throw RequestContentException()
                            }
                        }
                    }
                })
        } catch (exception: RuntimeException) {
            val responseData = exception.toErrorResponse()
            responseWriterManager.write(responseData, response)
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun RuntimeException.toErrorResponse(): Response<Unit> {
        return when (this) {
            is RequestMethodException ->
                Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.REQUEST_METHOD_ERROR)
            is RequestContentException, is NullPointerException ->
                Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.REQUEST_BODY_ERROR)
            else -> Response.fail(ExceptionMessage.INTERNAL_ERROR)
        }
    }
}
