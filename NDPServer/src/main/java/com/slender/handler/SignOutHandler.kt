package com.slender.handler

import com.slender.constant.other.RedisKey
import com.slender.exception.authentication.login.LoginExpiredException
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.core.Authentication
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.security.web.authentication.logout.LogoutHandler
import org.springframework.stereotype.Component

@Component
class SignOutHandler(
    private val redisTemplate: StringRedisTemplate
) : LogoutHandler {
    override fun logout(
        request: HttpServletRequest?,
        response: HttpServletResponse?,
        authentication: Authentication
    ) {
        val uid = SecurityContextHolder.getContext().authentication.principal as Long
        val loginData = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid)
        if (loginData != null) redisTemplate.delete(RedisKey.Authentication.USER_LOGIN_CACHE + uid)
        else throw LoginExpiredException()
    }
}
