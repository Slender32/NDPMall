package com.slender.dto.user;

import com.slender.annotation.Email;
import com.slender.annotation.Phone;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class UserRegisterRequest {
    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "slender")
    @NotBlank(message = "用户名不能为空")
    @Size(min = 2, max = 20, message = "用户名长度必须在2到20个字符之间")
    private String userName;

    @Schema(description = "密码（BCrypt加密后存储）", requiredMode = Schema.RequiredMode.REQUIRED, example = "e10adc3949ba59abbe56e057f20f883e")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Email
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED, example = "20377794@qq.com")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Phone
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800138000")
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    @NotNull
    @Range(min = 100000, max = 999999, message = "验证码格式错误")
    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    private Integer captcha;
}
