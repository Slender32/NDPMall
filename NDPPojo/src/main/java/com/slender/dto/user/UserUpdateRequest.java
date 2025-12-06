package com.slender.dto.user;

import com.slender.constant.user.UserConstant;
import com.slender.enumeration.user.UserGender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDate;

@Data
public class UserUpdateRequest {

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "slender")
    @Length(min = 2, max = 20, message = "用户名长度必须在2到20个字符之间")
    private String userName;

    @Schema(description = "年龄", minimum = "0", maximum = "150", example = "25")
    @Range(min = 0, max = 150, message = "年龄不合理")
    private Integer age;

    @Schema(description = "性别：0-保密 1-男 2-女",
            allowableValues = {UserConstant.Gender.FEMALE, UserConstant.Gender.MALE, UserConstant.Gender.FEMALE},
            example = UserConstant.Gender.PROTECTED)
    private UserGender gender;

    @Schema(description = "生日", example = "1995-08-20")
    @PastOrPresent
    private LocalDate birthday;
}
