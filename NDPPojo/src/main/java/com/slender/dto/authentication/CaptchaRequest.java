package com.slender.dto.authentication;

import com.slender.annotation.Email;
import com.slender.enumeration.authentication.CaptchaType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class CaptchaRequest {
    @Email
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotNull(message = "验证码类型不能为空")
    private CaptchaType type;
}
