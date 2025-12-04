package com.slender.handler;

import com.slender.constant.other.RedisKey;
import com.slender.exception.authentication.login.LoginExpiredException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class SignOutHandler implements LogoutHandler {
    private final StringRedisTemplate redisTemplate;
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        Long uid = (Long) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String loginData = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid);
        if(loginData != null) redisTemplate.delete(RedisKey.Authentication.USER_LOGIN_CACHE + uid);
        else throw new LoginExpiredException();
    }
}
