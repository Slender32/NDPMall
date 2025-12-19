package com.slender.dto.product;

import com.slender.constant.product.ProductConstant;
import com.slender.enumeration.product.ProductStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
public class ProductUpdateRequest {

    @Schema(description = "分类ID", example = "301")
    @Positive(message = "分类ID必须为正整数")
    private Long cid;

    @Schema(description = "产品名", requiredMode = Schema.RequiredMode.REQUIRED, example = "OPPO A5")
    @Length(min = 1, max = 20, message = "产品名长度必须在1到20个字符之间")
    private String productName;

    @Schema(description = "库存", minimum = "0", example = "100")
    @Positive(message = "库存必须为正整数")
    private Integer remain;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "199.99")
    @DecimalMin(value = "0.00", message = "价格不能为负数")
    private BigDecimal price;

    @Schema(description = "图片URL", example = "https://example.com/image.jpg")
    private String image;

    @Schema(description = "商品描述", example = "高保真音质，充电五分钟,通话两小时")
    private String description;

    @Schema(description = "状态：0-上架 1-下架",
            allowableValues = {ProductConstant.Status.LIST, ProductConstant.Status.DELIST},
            example = ProductConstant.Status.LIST)
    private ProductStatus status;
}
