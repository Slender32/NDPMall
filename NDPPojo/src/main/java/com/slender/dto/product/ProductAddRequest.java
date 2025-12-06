package com.slender.dto.product;

import com.slender.constant.product.ProductConstant;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.math.BigDecimal;

@Data
public class ProductAddRequest {
    @Schema(description = "分类ID", example = "301")
    @Positive(message = "分类ID必须为正整数")
    private Long cid= ProductConstant.DEFAULT_CATEGORY;

    @Schema(description = "产品名", requiredMode = Schema.RequiredMode.REQUIRED, example = "OPPO A5")
    @NotBlank(message = "产品名不能为空")
    @Length(min = 1, max = 20, message = "产品名长度必须在1到20个字符之间")
    private String productName;

    @Schema(description = "库存", minimum = "0", example = "100")
    @Positive(message = "库存必须为正整数")
    private Integer remain;

    @Schema(description = "价格", requiredMode = Schema.RequiredMode.REQUIRED, example = "199.99")
    @DecimalMin(value = "0.00", message = "价格不能为负数")
    @NotNull(message = "价格不能为空")
    private BigDecimal price;

    @Schema(description = "商品描述", example = "高保真音质，充电五分钟,通话两小时")
    private String description;
}
