package com.slender.utils;

import com.slender.constant.JwtConstant;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public final class JwtToolkit {
    private JwtToolkit(){}
    private static String getToken(String key,int expirationTime,Map<String,Object> data){
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                .claims(data)
                .expiration(new Date(System.currentTimeMillis()+expirationTime))
                .compact();
    }

    public static String getAccessToken(Long uid){
        return getToken(JwtConstant.ACCESS_KEY,JwtConstant.ACCESS_TOKEN_EXPIRATION_TIME,Map.of("uid",uid));
    }

    public static String getRefreshToken(Long uid){
        return getToken(JwtConstant.REFRESH_KEY,JwtConstant.REFRESH_TOKEN_EXPIRATION_TIME,Map.of("uid",uid));
    }

    public static Map<String,Object> parseToken(String key,String userToken) {
       return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(userToken)
                .getPayload();
    }
}
