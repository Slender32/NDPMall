package com.slender.handler;

import com.slender.config.manager.JsonParserManager;
import com.slender.config.manager.ResponseWriterManager;
import com.slender.constant.RedisKey;
import com.slender.constant.RedisTime;
import com.slender.entity.User;
import com.slender.message.FilterMessage;
import com.slender.model.JwtAuthenticationToken;
import com.slender.model.LoginDataCache;
import com.slender.result.Response;
import com.slender.utils.JwtToolkit;
import com.slender.vo.LoginData;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.stereotype.Component;

import java.time.Duration;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthSuccessHandler implements AuthenticationSuccessHandler {
    private final ResponseWriterManager responseWriterManager;
    private final StringRedisTemplate redisTemplate;
    private final JsonParserManager jsonParser;

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication){
        User user= (User) authentication.getPrincipal();
        LoginDataCache loginDataCache = new LoginDataCache(user.getUid(), user.getUserName(), user.getAuthority());
        redisTemplate.opsForValue().set(RedisKey.Authentication.USER_LOGIN_CACHE + user.getUid(),
                jsonParser.format(loginDataCache), Duration.ofMillis(RedisTime.Authentication.ACCESS_TOKEN_EXPIRE_TIME));
        SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(loginDataCache));
        responseWriterManager.write(Response.success(FilterMessage.LOGIN_SUCCESS,new LoginData(user.getUid(), user.getUserName(),
                JwtToolkit.getAccessToken(user.getUid()),
                JwtToolkit.getAccessToken(user.getUid()))),response);
    }
}
