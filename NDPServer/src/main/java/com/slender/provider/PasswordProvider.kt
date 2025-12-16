package com.slender.provider

import com.slender.constant.other.RedisKey
import com.slender.exception.authentication.login.LoginMisMatchException
import com.slender.exception.authentication.login.LoginPersistenceException
import com.slender.message.ExceptionMessage.getLoginMessage
import com.slender.model.token.PasswordAuthenticationToken
import com.slender.service.interfase.UserService
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.security.authentication.AuthenticationProvider
import org.springframework.security.core.Authentication
import org.springframework.security.core.AuthenticationException
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Component

@Component
class PasswordProvider(
    private val userService: UserService,
    private val passwordEncoder: PasswordEncoder,
    private val redisTemplate: StringRedisTemplate
) : AuthenticationProvider {

    @Throws(AuthenticationException::class)
    override fun authenticate(authentication: Authentication): Authentication {
        val token = authentication as PasswordAuthenticationToken
        return userService
            .getByDataBaseColumn(token.dataBaseColumn, token.authenticatedValue)
            .map { user ->
                redisTemplate.delete(RedisKey.Authentication.USER_BLOCK_CACHE + user.uid)

                val cache = redisTemplate.opsForValue()
                    .get(RedisKey.Authentication.USER_LOGIN_CACHE + user.uid)
                if (cache != null) throw LoginPersistenceException()

                if (!passwordEncoder.matches(token.credentials, user.password)) {
                    throw LoginMisMatchException(getLoginMessage(token.dataBaseColumn))
                }

                PasswordAuthenticationToken(user)
            }
            .orElseThrow { LoginMisMatchException(getLoginMessage(token.dataBaseColumn)) }
    }

    override fun supports(authentication: Class<*>): Boolean {
        return PasswordAuthenticationToken::class.java.isAssignableFrom(authentication)
    }
}