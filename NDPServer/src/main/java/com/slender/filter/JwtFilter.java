package com.slender.filter;

import com.slender.config.configuration.SecurityConfiguration;
import com.slender.config.manager.JsonParserManager;
import com.slender.config.manager.RequestConfigManager;
import com.slender.config.manager.ResponseWriterManager;
import com.slender.constant.other.JwtConstant;
import com.slender.constant.other.RedisKey;
import com.slender.constant.user.UserField;
import com.slender.exception.authentication.login.*;
import com.slender.exception.category.LoginException;
import com.slender.message.ExceptionMessage;
import com.slender.model.token.JwtAuthenticationToken;
import com.slender.model.cache.LoginDataCache;
import com.slender.result.Response;
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
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtFilter extends OncePerRequestFilter {
    private final RequestConfigManager requestConfigManager;
    private final ResponseWriterManager responseWriterManager;
    private final StringRedisTemplate redisTemplate;
    private final JsonParserManager jsonParser;
    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        boolean requireToken = requestConfigManager.findRequestConfig(request.getRequestURI())
                .map(RequestConfigManager.RequestConfig::requireToken).orElse(true);
        try {
            if(requireToken){
                String header = request.getHeader("Authorization");
                if(!StringUtils.hasText(header) || !header.startsWith("Bearer "))
                    throw new TokenNotFoundException();
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
        } catch (LoginException exception) {
            final Response<Void> responseData = switch (exception){
                case TokenNotFoundException _ -> Response.fail(HttpStatus.UNAUTHORIZED.value(), ExceptionMessage.TOKEN_NOT_FOUND);
                case TokenSignatureException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.TOKEN_SIGNATURE_ERROR);
                case TokenExpiredException _ -> Response.fail(HttpStatus.UNAUTHORIZED.value(), ExceptionMessage.TOKEN_EXPIRE_ERROR);
                case BlockException _ -> Response.fail(HttpStatus.FORBIDDEN.value(), ExceptionMessage.BLOCK_ERROR);
                case LoginNotExpiredException _ -> Response.fail(HttpStatus.BAD_REQUEST.value(), ExceptionMessage.LOGIN_NOT_EXPIRED_ERROR);
                case LoginExpiredException _ -> Response.fail(HttpStatus.UNAUTHORIZED.value(), ExceptionMessage.LOGIN_EXPIRED_ERROR);
                default -> Response.fail(ExceptionMessage.INTERNAL_ERROR);
            };
            responseWriterManager.write(responseData, response);
            return;
        }
        filterChain.doFilter(request, response);
    }
}
