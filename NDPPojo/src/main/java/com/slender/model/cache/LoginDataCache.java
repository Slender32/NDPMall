package com.slender.model.cache;

import com.slender.enumeration.user.UserAuthority;

public record LoginDataCache(
        Long uid,
        String userName,
        String email,
        UserAuthority authority
){}
