package com.slender.filter;

import com.slender.config.configuration.SecurityConfiguration;
import com.slender.config.manager.JsonParserManager;
import com.slender.config.manager.RequestConfigManager;
import com.slender.constant.other.JwtConstant;
import com.slender.constant.other.RedisKey;
import com.slender.constant.user.UserField;
import com.slender.exception.authentication.login.*;
import com.slender.model.token.JwtAuthenticationToken;
import com.slender.model.cache.LoginDataCache;
import com.slender.utils.JwtToolkit;
import com.slender.utils.StringToolkit;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.security.SignatureException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.jspecify.annotations.NonNull;
import org.jspecify.annotations.Nullable;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

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
            if(!StringUtils.hasText(header) || !header.startsWith("Bearer ")) throw new TokenNotFoundException();

            try {
                if(SecurityConfiguration.SecurityURLManager.REFRESH.equals(request.getRequestURI())){
                    String uid = JwtToolkit.parseToken(JwtConstant.REFRESH_KEY, header.substring(7))
                            .getOrDefault(UserField.UID, StringToolkit.getBlankString()).toString();
                    if(!StringUtils.hasText(uid)) throw new TokenNotFoundException();
                    String block = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_BLOCK_CACHE + uid);
                    if(block != null) throw new BlockException();
                    String cacheData = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid);
                    if(cacheData != null) throw new LoginNotExpiredException();
                    SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(Long.valueOf(uid)));
                }else{
                    String uid = JwtToolkit.parseToken(JwtConstant.ACCESS_KEY, header.substring(7))
                            .getOrDefault(UserField.UID, StringToolkit.getBlankString()).toString();
                    if(!StringUtils.hasText(uid)) throw new TokenNotFoundException();
                    String cacheData = redisTemplate.opsForValue().get(RedisKey.Authentication.USER_LOGIN_CACHE + uid);
                    if(cacheData == null) throw new LoginExpiredException();
                    LoginDataCache loginDataCache = jsonParser.parse(cacheData, LoginDataCache.class);
                    SecurityContextHolder.getContext().setAuthentication(new JwtAuthenticationToken(loginDataCache));
                }
            } catch (SignatureException _) {
                throw new TokenSignatureException();
            }catch (ExpiredJwtException _){
                throw new TokenExpiredException();
            }
        }
        filterChain.doFilter(request, response);
    }
}
