package com.slender.utils

import com.slender.constant.other.JwtConstant
import com.slender.constant.user.UserField
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.security.Keys
import java.nio.charset.StandardCharsets
import java.util.*

object JwtToolkit {

    private fun getToken(
        key: String,
        expirationTime: Int,
        data: Map<String, Any>
    ): String
        = Jwts.builder()
            .signWith(Keys.hmacShaKeyFor(key.toByteArray(StandardCharsets.UTF_8)))
            .claims(data)
            .expiration(Date(System.currentTimeMillis() + expirationTime))
            .compact()


    @JvmStatic
    fun getAccessToken(uid: Long): String
        = getToken(
            JwtConstant.ACCESS_KEY,
            JwtConstant.ACCESS_TOKEN_EXPIRATION_TIME,
            mapOf(UserField.UID to uid)
        )


    fun getRefreshToken(uid: Long): String
        = getToken(
            JwtConstant.REFRESH_KEY,
            JwtConstant.REFRESH_TOKEN_EXPIRATION_TIME,
            mapOf(UserField.UID to uid)
        )


    fun parseToken(key: String, userToken: String): Map<String, Any>
        = Jwts.parser()
            .verifyWith(Keys.hmacShaKeyFor(key.toByteArray(StandardCharsets.UTF_8)))
            .build()
            .parseSignedClaims(userToken)
            .payload

}