package com.slender.model.message;

import com.slender.enumeration.authentication.CaptchaType;

public record EmailMessage (
    CaptchaType type,
    String toEmail
){}
