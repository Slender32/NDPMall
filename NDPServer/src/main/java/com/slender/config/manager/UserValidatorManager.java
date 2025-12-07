package com.slender.config.manager;

import com.slender.constant.other.RedisKey;
import com.slender.enumeration.authentication.CaptchaType;
import com.slender.exception.authentication.captcha.CaptchaMisMatchException;
import com.slender.exception.authentication.captcha.CaptchaNotFoundException;
import com.slender.exception.authentication.login.LocalCacheNotFoundException;
import com.slender.exception.authentication.login.LoginExpiredException;
import com.slender.model.cache.LoginDataCache;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Objects;

@Component
@RequiredArgsConstructor
public class UserValidatorManager {
    private final HashMap<Integer,String> keyCache = new HashMap<>();
    private final StringRedisTemplate redisTemplate;
    private final JsonParserManager jsonParser;

    public void validateCaptcha(Long uid, Integer captcha, CaptchaType type){
        String cache = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid);
        if (cache==null) throw new LoginExpiredException();

        LoginDataCache data = jsonParser.parse(cache, LoginDataCache.class);
        String localCaptcha = redisTemplate.opsForValue().get(type.getRedisKey() + data.email());
        if(localCaptcha ==null) throw new CaptchaNotFoundException();

        if(!Objects.equals(localCaptcha, captcha.toString())) throw new CaptchaMisMatchException();
        keyCache.put(captcha,type.getRedisKey() + data.email());
    }

    public void validateCaptcha(String email, Integer captcha){
        String localCaptcha = redisTemplate.opsForValue().get(RedisKey.Authentication.CAPTCHA_REGISTER_CACHE + email);
        if(localCaptcha ==null) throw new CaptchaNotFoundException();
        if(!Objects.equals(localCaptcha, captcha.toString())) throw new CaptchaMisMatchException();
        keyCache.put(captcha,RedisKey.Authentication.CAPTCHA_REGISTER_CACHE + email);
    }

    public LoginDataCache getLoginDataCache(Long uid){
        String cache = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid);
        if (cache==null) throw new LoginExpiredException();
        return jsonParser.parse(cache, LoginDataCache.class);
    }

    public void removeCaptcha(Integer captcha){
        String key = keyCache.getOrDefault(captcha,null);
        if(key==null) throw new LocalCacheNotFoundException();
        redisTemplate.delete(key);
    }
}
