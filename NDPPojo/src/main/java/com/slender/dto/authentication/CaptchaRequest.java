package com.slender.dto.authentication;

import com.slender.annotation.Email;
import com.slender.enumeration.authentication.CaptchaType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "验证码请求参数")
public class CaptchaRequest {
    @Email
    @NotBlank(message = "邮箱不能为空")
    @Schema(description = "用户邮箱", example = "user@qq.com", requiredMode = Schema.RequiredMode.REQUIRED)
    private String email;

    @NotNull(message = "验证码类型不能为空")
    @Schema(description = "验证码类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private CaptchaType type;
}

