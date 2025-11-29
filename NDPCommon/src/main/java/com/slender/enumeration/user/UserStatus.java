package com.slender.enumeration.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "用户状态")
public enum UserStatus {
    @Schema(description = "正常")
    NORMAL(0),
    @Schema(description = "冻结")
    FROZEN(1);

    @EnumValue
    private final int value;
}
