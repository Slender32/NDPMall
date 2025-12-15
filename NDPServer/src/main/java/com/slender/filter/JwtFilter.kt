package com.slender.filter

import com.slender.config.configuration.SecurityConfiguration
import com.slender.config.manager.JsonParserManager
import com.slender.config.manager.RequestConfigManager
import com.slender.config.manager.ResponseWriterManager
import com.slender.constant.other.JwtConstant
import com.slender.constant.other.RedisKey
import com.slender.constant.user.UserField
import com.slender.exception.authentication.login.*
import com.slender.exception.category.LoginException
import com.slender.message.ExceptionMessage
import com.slender.model.cache.LoginDataCache
import com.slender.model.token.JwtAuthenticationToken
import com.slender.result.Response
import com.slender.utils.JwtToolkit
import com.slender.utils.StringToolkit
import io.jsonwebtoken.ExpiredJwtException
import io.jsonwebtoken.security.SignatureException
import jakarta.servlet.FilterChain
import jakarta.servlet.ServletException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import lombok.RequiredArgsConstructor
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.http.HttpStatus
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.util.AntPathMatcher
import org.springframework.util.StringUtils
import org.springframework.web.filter.OncePerRequestFilter
import java.io.IOException

@Component
class JwtFilter(
    private val requestConfigManager: RequestConfigManager,
    private val responseWriterManager: ResponseWriterManager,
    private val redisTemplate: StringRedisTemplate,
    private val jsonParser: JsonParserManager
) : OncePerRequestFilter() {
    private val pathMatcher = AntPathMatcher()
    @Throws(ServletException::class, IOException::class)
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val requireToken = requestConfigManager.findRequestConfig(request.requestURI)
            .map(RequestConfigManager.RequestConfig::requireToken).orElse(true)
        try {
            if (requireToken) {
                val header = request.getHeader("Authorization")
                if (!StringUtils.hasText(header) || !header.startsWith("Bearer ")) throw TokenNotFoundException()
                try {
                    if (pathMatcher.match(SecurityConfiguration.REFRESH, request.requestURI)) {
                        val uid: String = JwtToolkit.parseToken(JwtConstant.REFRESH_KEY, header.substring(7))
                            .getOrDefault(UserField.UID, StringToolkit.getBlankString()).toString()
                        if (uid.isBlank()) throw TokenNotFoundException()
                        val block = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_BLOCK_CACHE + uid)
                        if (block != null) throw BlockException()
                        val cacheData = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid)
                        if (cacheData != null) throw LoginNotExpiredException()
                        SecurityContextHolder.getContext().authentication = JwtAuthenticationToken(uid.toLong())
                    } else {
                        val uid: String = JwtToolkit.parseToken(JwtConstant.ACCESS_KEY, header.substring(7))
                            .getOrDefault(UserField.UID, StringToolkit.getBlankString()).toString()
                        if (uid.isBlank()) throw TokenNotFoundException()
                        val cacheData = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid) ?: throw LoginExpiredException()
                        val loginDataCache = jsonParser.parse(cacheData, LoginDataCache::class.java)
                        SecurityContextHolder.getContext().authentication = JwtAuthenticationToken(loginDataCache)
                    }
                } catch (_: SignatureException) {
                    throw TokenSignatureException()
                } catch (_: ExpiredJwtException) {
                    throw TokenExpiredException()
                }
            }
        } catch (exception: LoginException) {
            val responseData = exception.toErrorResponse()
            responseWriterManager.write(responseData, response)
            return
        }
        filterChain.doFilter(request, response)
    }

    private fun LoginException.toErrorResponse(): Response<Unit>{
        return when (this) {
            is TokenNotFoundException -> Response.fail(HttpStatus.UNAUTHORIZED.value(), ExceptionMessage.TOKEN_NOT_FOUND)
            is TokenSignatureException -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.TOKEN_SIGNATURE_ERROR)
            is TokenExpiredException -> Response.fail(HttpStatus.UNAUTHORIZED.value(), ExceptionMessage.TOKEN_EXPIRE_ERROR)
            is BlockException -> Response.fail(HttpStatus.FORBIDDEN.value(), ExceptionMessage.BLOCK_ERROR)
            is LoginNotExpiredException -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.LOGIN_NOT_EXPIRED_ERROR)
            is LoginExpiredException -> Response.fail(HttpStatus.UNAUTHORIZED.value(), ExceptionMessage.LOGIN_EXPIRED_ERROR)
            else -> Response.fail(ExceptionMessage.INTERNAL_ERROR)
        }
    }
}
