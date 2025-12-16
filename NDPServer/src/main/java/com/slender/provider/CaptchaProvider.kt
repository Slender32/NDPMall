package com.slender.provider

import com.slender.constant.other.RedisKey
import com.slender.exception.authentication.captcha.CaptchaMisMatchException
import com.slender.exception.authentication.captcha.CaptchaNotFoundException
import com.slender.exception.authentication.login.EmailNotFoundException
import com.slender.exception.authentication.login.LoginPersistenceException
import com.slender.model.token.CaptchaAuthenticationToken
import com.slender.service.interfase.UserService
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.stereotype.Component

@Component
class CaptchaProvider(
    private val userService: UserService,
    private val redisTemplate: StringRedisTemplate
) : AuthenticationProvider {
    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication as CaptchaAuthenticationToken

        return userService.getByEmail(token.email)
            .map { user ->
                redisTemplate.delete(RedisKey.Authentication.USER_BLOCK_CACHE + user.uid)

                val cache = redisTemplate.opsForValue()
                    .get(RedisKey.Authentication.USER_LOGIN_CACHE + user.uid)
                if (cache != null) throw LoginPersistenceException()

                val captcha = redisTemplate.opsForValue()
                    .get(RedisKey.Authentication.CAPTCHA_LOGIN_CACHE + user.email)
                if (captcha == null) throw CaptchaNotFoundException()
                if (token.captcha.toString() != captcha) throw CaptchaMisMatchException()

                redisTemplate.delete(RedisKey.Authentication.CAPTCHA_LOGIN_CACHE + user.email)
                CaptchaAuthenticationToken(user)
            }
            .orElseThrow { EmailNotFoundException() }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return CaptchaAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}