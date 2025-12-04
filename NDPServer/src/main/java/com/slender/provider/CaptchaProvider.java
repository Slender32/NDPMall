package com.slender.provider;

import com.slender.constant.other.RedisKey;
import com.slender.exception.authentication.captcha.CaptchaMisMatchException;
import com.slender.exception.authentication.captcha.CaptchaNotFoundException;
import com.slender.exception.authentication.login.EmailNotFoundException;
import com.slender.exception.authentication.login.LoginPersistenceException;
import com.slender.model.token.CaptchaAuthenticationToken;
import com.slender.service.interfase.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class CaptchaProvider implements AuthenticationProvider {
    private final UserService userService;
    private final StringRedisTemplate redisTemplate;
    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        CaptchaAuthenticationToken token = (CaptchaAuthenticationToken) authentication;
        return userService.getByEmail(token.getEmail())
                .map(user -> {
                    redisTemplate.delete(RedisKey.Authentication.USER_BLOCK_CACHE + user.getUid());
                    String cache = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + user.getUid());
                    if (cache != null) throw new LoginPersistenceException();
                    String captcha = redisTemplate.opsForValue().get(RedisKey.Authentication.CAPTCHA_LOGIN_CACHE + user.getEmail());
                    if (captcha == null) throw new CaptchaNotFoundException();
                    if (!token.getCaptcha().toString().equals(captcha)) throw new CaptchaMisMatchException();
                    redisTemplate.delete(RedisKey.Authentication.CAPTCHA_LOGIN_CACHE + user.getEmail());
                    return new CaptchaAuthenticationToken(user);
                })
                .orElseThrow(EmailNotFoundException::new);
    }
    @Override
    public boolean supports(Class<?> authentication) {
        return (CaptchaAuthenticationToken.class.isAssignableFrom(authentication));
    }
}
