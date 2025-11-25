package com.slender.utils;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.Map;

public final class JwtToolkit {
    private JwtToolkit(){}
    public static String getToken(String key,int expirationTime,Map<String,Object> data){
        return Jwts.builder()
                .signWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                .claims(data)
                .expiration(new Date(System.currentTimeMillis()+expirationTime))
                .compact();
    }

    public static Map<String,Object> parseToken(String key,String userToken) {
       return Jwts.parser()
                .verifyWith(Keys.hmacShaKeyFor(key.getBytes(StandardCharsets.UTF_8)))
                .build()
                .parseSignedClaims(userToken)
                .getPayload();
    }
}
