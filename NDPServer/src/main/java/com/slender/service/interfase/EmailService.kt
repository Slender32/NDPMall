package com.slender.service.interfase

import com.slender.annotation.Email
import com.slender.enumeration.authentication.CaptchaType

interface EmailService {
    fun sendCaptcha(type: CaptchaType, @Email toEmail: String)
}
