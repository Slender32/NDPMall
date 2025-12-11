package com.slender.handler

import com.slender.config.manager.JsonParserManager
import com.slender.config.manager.ResponseWriterManager
import com.slender.constant.other.RedisKey
import com.slender.constant.other.RedisTime
import com.slender.entity.User
import com.slender.message.UserMessage
import com.slender.model.cache.LoginDataCache
import com.slender.model.token.JwtAuthenticationToken
import com.slender.result.Response
import com.slender.utils.JwtToolkit
import com.slender.vo.LoginData
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.AuthenticationSuccessHandler
import org.springframework.stereotype.Component

@Component
class AuthSuccessHandler(
    private val responseWriterManager: ResponseWriterManager,
    private val redisTemplate: StringRedisTemplate,
    private val jsonParser: JsonParserManager
) : AuthenticationSuccessHandler {

    override fun onAuthenticationSuccess(
        request: HttpServletRequest?,
        response: HttpServletResponse,
        authentication: Authentication
    ) {
        val user = authentication.principal as User
        val loginDataCache = LoginDataCache(user.uid, user.userName, user.email, user.authority)
        redisTemplate.opsForValue().set(
            RedisKey.Authentication.USER_LOGIN_CACHE + user.uid,
            jsonParser.format(loginDataCache), RedisTime.Authentication.ACCESS_TOKEN_EXPIRE_TIME
        )
        SecurityContextHolder.getContext().authentication = JwtAuthenticationToken(loginDataCache)
        responseWriterManager.write(
            Response.success(
                UserMessage.LOGIN_SUCCESS, LoginData(
                    user.uid, user.userName,
                    JwtToolkit.getAccessToken(user.uid),
                    JwtToolkit.getRefreshToken(user.uid)
                )
            ), response
        )
    }
}
