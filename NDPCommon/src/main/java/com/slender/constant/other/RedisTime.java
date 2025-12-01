package com.slender.constant.other;

import com.slender.utils.TimeToolkit;

import java.time.Duration;

public interface RedisTime {
    interface Authentication{
        Duration CAPTCHA_EXPIRE_TIME= Duration.ofMillis(TimeToolkit.Unit.MINUTE*5);
        Duration CAPTCHA_REQUEST_TIME= Duration.ofMillis(TimeToolkit.Unit.MINUTE);
        Duration ACCESS_TOKEN_EXPIRE_TIME = Duration.ofMillis(TimeToolkit.Unit.HOUR/2);
        Duration REFRESH_TOKEN_EXPIRE_TIME = Duration.ofMillis(TimeToolkit.Unit.DAY);
    }
    
    interface Entity{
        Duration EXPIRE_TIME = Duration.ofMillis(TimeToolkit.Unit.HOUR/2);
    }
}
