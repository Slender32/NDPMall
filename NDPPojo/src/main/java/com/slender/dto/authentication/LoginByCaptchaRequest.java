package com.slender.dto.authentication;

import com.slender.annotation.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class LoginByCaptchaRequest {
    @Email
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @NotNull(message = "验证码不能为空")
    @Range(min = 100000, max = 999999, message = "验证码长度必须为6位")
    private Integer captcha;
}
