package com.slender.enumeration.authentication;

import com.slender.constant.other.EmailConstant;
import com.slender.constant.other.RedisKey;

public enum CaptchaType {
    LOGIN,
    REGISTER,
    RESET;

    public String getMessage(){
        return switch (this){
            case LOGIN -> EmailConstant.LOGIN;
            case REGISTER -> EmailConstant.REGISTER;
            case RESET -> EmailConstant.RESET;
        };
    }

    public String getRedisKey(){
        return switch (this){
            case LOGIN -> RedisKey.Authentication.CAPTCHA_LOGIN_CACHE;
            case REGISTER -> RedisKey.Authentication.CAPTCHA_REGISTER_CACHE;
            case RESET -> RedisKey.Authentication.CAPTCHA_RESET_CACHE;
        };
    }
}
