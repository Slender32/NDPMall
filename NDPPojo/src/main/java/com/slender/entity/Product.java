package com.slender.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "商品信息")
public class Product {

    @Schema(description = "产品ID", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer pid;

    @Schema(description = "商家ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "201")
    @NotNull(message = "商家ID不能为空")
    @Positive(message = "商家ID必须为正整数")
    private Integer mid;

    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "301")
    @NotNull(message = "分类ID不能为空")
    @Positive(message = "分类ID必须为正整数")
    private Integer cid;

    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不得超前于当前时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "更新时间不得超前于当前时间")
    private LocalDateTime updateTime;

    @Schema(description = "产品名", requiredMode = Schema.RequiredMode.REQUIRED, example = "无线蓝牙耳机")
    @NotBlank(message = "产品名不能为空")
    @Size(min = 1, max = 20, message = "产品名长度必须在1到20个字符之间")
    private String productName;

    @Schema(description = "库存", minimum = "0", example = "100")
    @NotNull(message = "库存不能为空")
    @Min(value = 0, message = "库存不能为负数")
    private Integer remain;

    @Schema(description = "销量", minimum = "0", example = "50", accessMode = Schema.AccessMode.READ_ONLY)
    @Min(value = 0, message = "销量不能为负数")
    private Integer saleAmount;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "199.99")
    @NotNull(message = "价格不能为空")
    @DecimalMin(value = "0.00", message = "价格不能为负数")
    private BigDecimal price;

    @Schema(description = "商品描述", example = "高保真音质，续航30小时")
    private String description;

    @Schema(description = "状态：0-上架 1-下架",
            allowableValues = {"0", "1"},
            example = "0")
    @NotNull(message = "状态不能为空")
    @Min(value = 0, message = "状态值无效")
    @Max(value = 1, message = "状态值无效")
    private Integer status;

    @Schema(description = "删除标记：0-未删除 1-已删除",
            allowableValues = {"0", "1"},
            accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull
    @Min(0)
    @Max(1)
    private Integer deleted;
}
