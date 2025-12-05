package com.slender.enumeration.authentication;

import com.slender.constant.other.EmailConstant;
import com.slender.constant.other.RedisKey;

public enum CaptchaType {
    LOGIN,
    REGISTER,
    LOGOFF,
    RESET;

    public String getMessage(){
        return switch (this){
            case LOGIN -> EmailConstant.LOGIN;
            case REGISTER -> EmailConstant.REGISTER;
            case LOGOFF -> EmailConstant.LOGOFF;
            case RESET -> EmailConstant.RESET;
        };
    }

    public String getRedisKey(){
        return switch (this){
            case LOGIN -> RedisKey.Authentication.CAPTCHA_LOGIN_CACHE;
            case REGISTER -> RedisKey.Authentication.CAPTCHA_REGISTER_CACHE;
            case LOGOFF -> RedisKey.Authentication.CAPTCHA_LOGOFF_CACHE;
            case RESET -> RedisKey.Authentication.CAPTCHA_RESET_CACHE;
        };
    }
}
