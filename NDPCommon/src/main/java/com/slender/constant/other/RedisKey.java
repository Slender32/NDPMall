package com.slender.constant.other;

public interface RedisKey {

     interface Authentication{
         String USER_LOGIN_CACHE="auth:";
         String CAPTCHA_REQUEST_CACHE="captcha:request:";
         String CAPTCHA_LOGIN_CACHE ="captcha:login:";
         String CAPTCHA_REGISTER_CACHE ="captcha:register:";
         String CAPTCHA_RESET_CACHE ="captcha:reset:";
         String CAPTCHA_LOGOFF_CACHE = "captcha:logoff:";
         String USER_BLOCK_CACHE = "block:";
     }
}
