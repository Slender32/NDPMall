package com.slender.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.slender.constant.other.EntityConstant;
import com.slender.constant.product.ProductConstant;
import com.slender.dto.product.ProductListRequest;
import com.slender.enumeration.DeleteStatus;
import com.slender.enumeration.product.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Schema(description = "商品信息")
@NoArgsConstructor
public class Product implements Serializable {

    @Schema(description = "产品ID", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    @TableId("pid")
    private Long pid;

    @Schema(description = "商家ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "201")
    @NotNull(message = "商家ID不能为空")
    @Positive(message = "商家ID必须为正整数")
    private Long mid;

    @Schema(description = "分类ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "301")
    @NotNull(message = "分类ID不能为空")
    @Positive(message = "分类ID必须为正整数")
    private Long cid;

    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不得超前于当前时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "更新时间不得超前于当前时间")
    private LocalDateTime updateTime;

    @Schema(description = "产品名", requiredMode = Schema.RequiredMode.REQUIRED, example = "无线蓝牙耳机")
    @NotBlank(message = "产品名不能为空")
    @Length(min = 1, max = 20, message = "产品名长度必须在1到20个字符之间")
    private String productName;

    @Schema(description = "库存", minimum = "0", example = "100")
    @Positive(message = "库存必须为正整数")
    private Integer remain;

    @Schema(description = "销量", minimum = "0", example = "50", accessMode = Schema.AccessMode.READ_ONLY)
    @Positive(message = "销量必须为正整数")
    private Integer saleAmount;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "199.99")
    @DecimalMin(value = "0.00", message = "价格不能为负数")
    private BigDecimal price;

    @Schema(description = "商品描述", example = "高保真音质，续航30小时")
    private String description;

    @Schema(description = "状态：0-上架 1-下架",
            allowableValues = {ProductConstant.Status.LIST, ProductConstant.Status.DELIST},
            example = ProductConstant.Status.LIST)
    @NotNull(message = "状态不能为空")
    private ProductStatus status;

    @Schema(description = "删除标记：0-未删除 1-已删除",
            allowableValues = {EntityConstant.Delete.DELETED, EntityConstant.Delete.NORMAL},
            accessMode = Schema.AccessMode.READ_ONLY)
    @NotNull
    private DeleteStatus deleted;

    public Product(Long uid, ProductListRequest request) {
        this.mid = uid;
        this.cid = request.getCid();
        this.productName = request.getProductName();
        this.remain = request.getRemain();
        this.price = request.getPrice();
        this.description = request.getDescription();
        this.updateTime = LocalDateTime.now();
    }

}
