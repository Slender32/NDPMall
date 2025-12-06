package com.slender.dto.order;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderCreateRequest {

    @Schema(description = "购买产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "456")
    @NotNull(message = "产品ID不能为空")
    @Positive(message = "产品ID必须为正整数")
    private Long pid;

    @Schema(description = "收货地址ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "789")
    @NotNull(message = "收货地址ID不能为空")
    @Positive(message = "收货地址ID必须为正整数")
    private Long aid;

    @Schema(description = "购买数量", requiredMode = Schema.RequiredMode.REQUIRED, example = "2")
    @NotNull(message = "购买数量不能为空")
    @Min(value = 1, message = "购买数量至少为1")
    private Integer quantity;

    @Schema(description = "总金额", requiredMode = Schema.RequiredMode.REQUIRED, example = "299.99")
    @NotNull(message = "总金额不能为空")
    @DecimalMin(value = "0.00", message = "总金额必须大于0")
    private BigDecimal totalAmount;
}
