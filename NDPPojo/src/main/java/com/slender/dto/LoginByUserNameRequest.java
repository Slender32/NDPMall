package com.slender.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "登录信息")
public class LoginByUserNameRequest {
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "slender")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2到20个字符之间")
    private String userName;

    @Schema(description = "密码（BCrypt加密后存储）", requiredMode = Schema.RequiredMode.REQUIRED, example = "e10adc3949ba59abbe56e057f20f883e")
    @NotBlank(message = "密码不能为空")
    private String password;
}
