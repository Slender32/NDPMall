package com.slender.enumeration.order;

import com.baomidou.mybatisplus.annotation.EnumValue;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
@Schema(description = "订单状态")
public enum OrderStatus {
    @Schema(description = "未支付")
    UNPAID(0),
    @Schema(description = "支付成功")
    PAID_SUCCESS(1),
    @Schema(description = "支付失败")
    PAID_FAIL(2),
    @Schema(description = "已取消")
    CANCEL(3);

    @EnumValue
    private final int value;
}

