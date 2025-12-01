package com.slender.enumeration;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "删除状态")
public enum DeleteStatus {
    @Schema(description = "未删除")
    NORMAL(0),
    @Schema(description = "已删除")
    DELETED(1);

    @EnumValue
    private final int value;
}
