package com.slender.dto.authentication;

import com.slender.annotation.Email;
import com.slender.annotation.Phone;
import com.slender.enumeration.authentication.LoginType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import org.hibernate.validator.constraints.Length;
import lombok.Data;

@Data
@Schema(description = "登录信息")
public class LoginByPasswordRequest{
    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456xyz")
    @NotBlank(message = "密码不能为空")
    private String password;

    @Schema(description = "登录类型", requiredMode = Schema.RequiredMode.REQUIRED, example = "USER_NAME")
    @NotNull(message = "登录类型不能为空")
    private LoginType type;

    @Schema(description = "用户名", example = "slender")
    @Length(min = 2, max = 20, message = "用户名长度必须在2到20个字符之间")
    private String userName;

    @Email
    @Schema(description = "邮箱", example = "20377794@qq.com")
    private String email;

    @Phone
    @Schema(description = "手机号", example = "13800138000")
    private String phoneNumber;

    @Schema(hidden = true)
    public String getAuthenticationValue(){
        return switch (type){
            case USER_NAME -> userName;
            case PHONE_NUMBER -> phoneNumber;
            case EMAIL -> email;
        };
    }
}
