package com.slender.dto.user;

import com.slender.annotation.Email;
import com.slender.annotation.Phone;
import com.slender.enumeration.authentication.ResetType;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class UserResetRequest {
    @NotNull
    @Range(min = 100000, max = 999999, message = "验证码格式错误")
    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    private Integer captcha;

    @NotNull
    @Schema(description = "重置类型", requiredMode = Schema.RequiredMode.REQUIRED)
    private ResetType type;

    @Email
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED, example = "20377794@qq.com")
    private String email;

    @Phone
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800138000")
    private String phoneNumber;

    @Schema(description = "密码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    private String password;

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "slender")
    @Size(min = 2, max = 20, message = "用户名长度必须在2到20个字符之间")
    private String userName;

    @Schema(hidden = true)
    public String getValue(){
        return switch (this.type){
            case USER_NAME -> this.userName;
            case PASSWORD -> this.password;
            case PHONE_NUMBER -> this.phoneNumber;
            case EMAIL -> this.email;
        };
    }
}
