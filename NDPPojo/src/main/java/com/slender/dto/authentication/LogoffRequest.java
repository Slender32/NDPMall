package com.slender.dto.authentication;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
@Schema(description = "注销请求参数")
public class LogoffRequest {

    @NotNull(message = "验证码不能为空")
    @Range(min = 100000, max = 999999, message = "验证码长度必须为6位")
    @Schema(description = "验证码", example = "123456", requiredMode = Schema.RequiredMode.REQUIRED)
    private Integer captcha;
}

