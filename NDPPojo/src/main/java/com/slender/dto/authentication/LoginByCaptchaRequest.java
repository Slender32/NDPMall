package com.slender.dto.authentication;

import com.slender.annotation.Email;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Schema(description = "邮箱验证码登录请求参数")
public class LoginByCaptchaRequest {
    @Email
    @NotBlank(message = "邮箱不能为空")
    @Schema(description = "用户邮箱", example = "user@example.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotNull(message = "验证码不能为空")
    @Range(min = 100000, max = 999999, message = "验证码长度必须为6位")
    @Schema(description = "验证码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer captcha;
}

