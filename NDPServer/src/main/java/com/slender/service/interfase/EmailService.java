package com.slender.service.interfase;

import com.slender.annotation.Email;
import com.slender.enumeration.authentication.CaptchaType;

public interface EmailService {
    void sendCaptcha(CaptchaType type, @Email String toEmail);
}
