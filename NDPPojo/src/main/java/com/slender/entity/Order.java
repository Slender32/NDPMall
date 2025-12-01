package com.slender.entity;
import com.baomidou.mybatisplus.annotation.TableId;
import com.slender.constant.order.OrderConstant;
import com.slender.constant.other.EntityConstant;
import com.slender.enumeration.DeleteStatus;
import com.slender.enumeration.order.OrderStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "订单信息")
public class Order {

    @Schema(description = "订单ID（数据库主键）", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    @TableId("bid")
    private Long bid;

    @Schema(description = "下单时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不得超前于当前时间")
    private LocalDateTime createTime;

    @Schema(description = "下单用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "123")
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正整数")
    private Long uid;

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

    @Schema(description = "订单状态：0-未支付 1-支付成功 2-支付失败 3-已取消",
            allowableValues = {OrderConstant.Status.UNPAID, OrderConstant.Status.PAID_SUCCESS, OrderConstant.Status.PAID_FAIL, OrderConstant.Status.CANCEL},
            example = OrderConstant.Status.UNPAID)
    private OrderStatus status;


    @Schema(description = "删除标记",
            allowableValues = {EntityConstant.Delete.DELETED, EntityConstant.Delete.NORMAL},
            accessMode = Schema.AccessMode.READ_ONLY)
    private DeleteStatus deleted;
}
