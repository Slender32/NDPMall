package com.slender.enumeration.user;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "性别")
public enum UserGender {
    @Schema(description = "保密")
    PROTECTED(0),
    @Schema(description = "男")
    MALE(1),
    @Schema(description = "女")
    FEMALE(2);

    @EnumValue
    private final int value;
}
