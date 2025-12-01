package com.slender.enumeration.product;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "商品状态")
public enum ProductStatus {
    @Schema(description = "上架")
    LIST(0),
    @Schema(description = "下架")
    DELIST(1);

    @EnumValue
    private final int value;
}
