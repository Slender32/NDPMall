package com.slender.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.slender.annotation.Email;
import com.slender.annotation.Phone;
import com.slender.constant.other.EntityConstant;
import com.slender.constant.user.UserConstant;
import com.slender.dto.user.UserRegisterRequest;
import com.slender.enumeration.user.UserAuthority;
import com.slender.enumeration.DeleteStatus;
import com.slender.enumeration.user.UserGender;
import com.slender.enumeration.user.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Schema(description = "用户信息")
@NoArgsConstructor
public class User {

    @Schema(description = "用户ID", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    @TableId(value = "uid")
    private Long uid;

    @Schema(description = "用户名", requiredMode = Schema.RequiredMode.REQUIRED, example = "slender")
    @NotBlank(message = "用户名不能为空")
    @Length(min = 2, max = 20, message = "用户名长度必须在2到20个字符之间")
    private String userName;

    @Schema(description = "密码（BCrypt加密后存储）", requiredMode = Schema.RequiredMode.REQUIRED, example = "e10adc3949ba59abbe56e057f20f883e")
    @NotBlank(message = "密码不能为空")
    @Length(min = 32, max = 60, message = "密码格式不合法")
    private String password;

    @Email
    @Schema(description = "邮箱", requiredMode = Schema.RequiredMode.REQUIRED, example = "20377794@qq.com")
    @NotBlank(message = "邮箱不能为空")
    private String email;

    @Phone
    @Schema(description = "手机号", requiredMode = Schema.RequiredMode.REQUIRED, example = "13800138000")
    @NotBlank(message = "手机号不能为空")
    private String phoneNumber;

    @Schema(description = "最后修改时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "更新时间不得超前于当前时间")
    private LocalDateTime updateTime;

    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不得超前于当前时间")
    private LocalDateTime createTime;

    @Schema(description = "头像")
    @Length(max = 255, message = "头像地址长度不能超过255个字符")
    private String avatar;

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

    @Schema(description = "账户余额", example = "100.50")
    @DecimalMin(value = "0.00", message = "余额不能为负数")
    private BigDecimal balance;

    @Schema(description = "身份权限：0-普通用户 1-商家 2-管理员",
            allowableValues = {UserConstant.Authority.USER, UserConstant.Authority.MERCHANT, UserConstant.Authority.ADMINISTRATION},
            example = UserConstant.Authority.USER)
    private UserAuthority authority;

    @Schema(description = "删除标记",
            allowableValues = {EntityConstant.Delete.DELETED, EntityConstant.Delete.NORMAL},
            accessMode = Schema.AccessMode.READ_ONLY)
    private DeleteStatus deleted;

    @Schema(description = "账户状态：0-正常 1-冻结",
            allowableValues = {UserConstant.Status.NORMAL, UserConstant.Status.FROZEN},
            example = UserConstant.Status.NORMAL)
    private UserStatus status;

    public User(UserRegisterRequest userRegisterRequest) {
        this.userName= userRegisterRequest.getUserName();
        this.password= userRegisterRequest.getPassword();
        this.email= userRegisterRequest.getEmail();
        this.phoneNumber= userRegisterRequest.getPhoneNumber();
        updateTime=LocalDateTime.now();
        createTime=LocalDateTime.now();
    }
}
