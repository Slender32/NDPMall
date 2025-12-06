package com.slender.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class ProductPageRequest {

    @Schema(description = "页码", example = "1")
    @Positive(message = "页码必须为正整数")
    private Integer page;

    @Schema(description = "每页大小", example = "10")
    @Positive(message = "每页大小必须为正整数")
    @Range(max = 100, min = 1, message = "每页大小错误")
    private Integer pageSize;

    @Schema(description = "排序方式：true-升序，false-降序", example = "true")
    private Boolean order;

    @Schema(description = "商家ID", example = "201")
    @Positive(message = "商家ID必须为正整数")
    private Long mid;

    @Schema(description = "分类ID", example = "301")
    @Positive(message = "分类ID必须为正整数")
    private Long cid;

    @Schema(description = "最早创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不得超前于当前时间")
    private LocalDateTime createTimeStart;

    @Schema(description = "最晚创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不得超前于当前时间")
    private LocalDateTime createTimeEnd;

    @Schema(description = "产品名", example = "无线蓝牙耳机")
    @Length(min = 1, max = 20, message = "产品名长度必须在1到20个字符之间")
    private String productName;

    @Schema(description = "最小库存", minimum = "0", example = "100")
    @Positive(message = "库存必须为正整数")
    private Integer remainStart;

    @Schema(description = "最大库存", minimum = "0", example = "100")
    @Positive(message = "库存必须为正整数")
    private Integer remainEnd;

    @Schema(description = "最小销量", minimum = "0", example = "50", accessMode = Schema.AccessMode.READ_ONLY)
    @Positive(message = "销量必须为正整数")
    private Integer saleAmountStart;

    @Schema(description = "最大销量", minimum = "0", example = "50", accessMode = Schema.AccessMode.READ_ONLY)
    @Positive(message = "销量必须为正整数")
    private Integer saleAmountEnd;

    @Schema(description = "最小价格", example = "199.99")
    @DecimalMin(value = "0.00", message = "价格不能为负数")
    private BigDecimal priceStart;

    @Schema(description = "最大价格", example = "199.99")
    @DecimalMin(value = "0.00", message = "价格不能为负数")
    private BigDecimal priceEnd;
}
