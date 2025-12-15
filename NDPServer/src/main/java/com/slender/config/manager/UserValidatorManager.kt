package com.slender.config.manager

import com.slender.constant.other.RedisKey
import com.slender.enumeration.authentication.CaptchaType
import com.slender.exception.authentication.captcha.CaptchaMisMatchException
import com.slender.exception.authentication.captcha.CaptchaNotFoundException
import com.slender.exception.authentication.login.LocalCacheNotFoundException
import com.slender.exception.authentication.login.LoginExpiredException
import com.slender.model.cache.LoginDataCache
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Component

@Component
class UserValidatorManager(
    private val redisTemplate: StringRedisTemplate,
    private val jsonParser: JsonParserManager
) {
    private val keyCache = HashMap<Int, String>()
    fun validateCaptcha(uid: Long, captcha: Int, type: CaptchaType) {
        val cache = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid)
            ?: throw LoginExpiredException()
        val data = jsonParser.parse(cache, LoginDataCache::class.java)
        val localCaptcha = redisTemplate.opsForValue().get(type.redisKey + data.email)
            ?: throw CaptchaNotFoundException()
        if (localCaptcha != captcha.toString()) throw CaptchaMisMatchException()
        keyCache[captcha] = type.redisKey + data.email
    }

    fun validateCaptcha(email: String, captcha: Int) {
        val localCaptcha = redisTemplate.opsForValue()
            .get(RedisKey.Authentication.CAPTCHA_REGISTER_CACHE + email)
            ?: throw CaptchaNotFoundException()
        if (localCaptcha != captcha.toString()) throw CaptchaMisMatchException()
        keyCache[captcha] = RedisKey.Authentication.CAPTCHA_REGISTER_CACHE + email
    }

    fun getLoginDataCache(uid: Long): LoginDataCache {
        val cache = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid)
            ?: throw LoginExpiredException()
        return jsonParser.parse(cache, LoginDataCache::class.java)
    }

    fun removeCaptcha(captcha: Int) {
        val key = keyCache[captcha] ?: throw LocalCacheNotFoundException()
        redisTemplate.delete(key)
    }
}