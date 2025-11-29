package com.slender.filter;

import com.slender.config.configuration.SecurityConfiguration;
import com.slender.config.manager.JsonParserManager;
import com.slender.config.manager.RequestConfigManager;
import com.slender.constant.JwtConstant;
import com.slender.constant.RedisKey;
import com.slender.constant.user.UserField;
import com.slender.exception.LoginStatusException;
import com.slender.exception.TokenMissingException;
import com.slender.model.JwtAuthenticationToken;
import com.slender.model.LoginDataCache;
import com.slender.utils.JwtToolkit;
import com.slender.utils.StringToolkit;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final RequestConfigManager requestConfigManager;
    private final StringRedisTemplate redisTemplate;
    private final JsonParserManager jsonParser;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @Nullable HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        boolean requireToken = requestConfigManager.findRequestConfig(request.getRequestURI())
                .map(RequestConfigManager.RequestConfig::requireToken).orElse(true);
        if(requireToken){
            String header = request.getHeader("Authorization");
            if(!StringUtils.hasText(header) || !header.startsWith("Bearer "))
                throw new TokenMissingException("请先登录");
            if(SecurityConfiguration.SecurityURLManager.REFRESH.equals(request.getRequestURI())){
                String uid = JwtToolkit.parseToken(JwtConstant.REFRESH_KEY, header.substring(7))
                        .getOrDefault(UserField.UID, StringToolkit.getBlankString()).toString();
                if(!StringUtils.hasText(uid)) throw new TokenMissingException("请先登录");
                String cacheData = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid);
                if(cacheData != null) throw new LoginStatusException();
                SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(Long.valueOf(uid)));
            }else{
                String uid = JwtToolkit.parseToken(JwtConstant.ACCESS_KEY, header.substring(7))
                        .getOrDefault(UserField.UID, StringToolkit.getBlankString()).toString();
                if(!StringUtils.hasText(uid)) throw new TokenMissingException("请先登录");
                String cacheData = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid);
                LoginDataCache loginDataCache = jsonParser.parse(cacheData, LoginDataCache.class);
                SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(loginDataCache));
            }
        }
        filterChain.doFilter(request, response);
    }
}
