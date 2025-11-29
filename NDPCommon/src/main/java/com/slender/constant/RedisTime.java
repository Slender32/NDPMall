package com.slender.constant;

import com.slender.utils.TimeToolkit;

public interface RedisTime {
    interface Authentication{
        int CAPTCHA_EXPIRE_TIME= TimeToolkit.Unit.MINUTE*5;
        int CAPTCHA_REQUEST_TIME= TimeToolkit.Unit.MINUTE;
        int ACCESS_TOKEN_EXPIRE_TIME = TimeToolkit.Unit.HOUR/2;
        int REFRESH_TOKEN_EXPIRE_TIME = TimeToolkit.Unit.DAY;
    }
}
