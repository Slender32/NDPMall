package com.slender.dto.order;

import com.slender.constant.order.OrderConstant;
import com.slender.enumeration.order.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

@Data
public class OrderUpdateRequest {
    @Schema(description = "收货地址ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "789")
    @Positive(message = "收货地址ID必须为正整数")
    private Long aid;

    @Schema(description = "购买数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @Min(value = 1, message = "购买数量至少为1")
    private Integer quantity;

    @Schema(description = "订单状态：0-未支付 1-支付成功 2-支付失败 3-已取消",
            allowableValues = {OrderConstant.Status.UNPAID, OrderConstant.Status.PAID_SUCCESS, OrderConstant.Status.PAID_FAIL, OrderConstant.Status.CANCEL},
            example = OrderConstant.Status.UNPAID)
    private OrderStatus status;
}
