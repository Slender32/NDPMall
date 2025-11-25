package com.slender.enumeration.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.slender.constant.user.UserConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;

@Getter
@RequiredArgsConstructor
@Schema(description = "用户权限")
public enum UserAuthority implements GrantedAuthority {
    @Schema(description = "普通用户")
    USER(0),
    @Schema(description = "商家")
    MERCHANT(1),
    @Schema(description = "管理员")
    ADMINISTRATION(2);

    @EnumValue
    private final int value;

    @Override
    public String getAuthority() {
        return switch (this.value){
            case 1 -> UserConstant.Authority.MERCHANT;
            case 2 -> UserConstant.Authority.ADMINISTRATION;
            default -> UserConstant.Authority.USER;
        };
    }
}
