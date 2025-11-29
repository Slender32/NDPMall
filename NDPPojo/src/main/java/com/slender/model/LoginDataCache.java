package com.slender.model;

import com.slender.enumeration.user.UserAuthority;

public record LoginDataCache(
        Long uid,
        String userName,
        UserAuthority authority
){}
