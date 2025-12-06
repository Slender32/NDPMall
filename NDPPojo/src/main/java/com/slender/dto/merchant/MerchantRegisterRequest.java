package com.slender.dto.merchant;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class MerchantRegisterRequest {
    @Schema(description = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "波力佳音专卖店")
    @NotBlank(message = "店铺名称不能为空")
    @Length(min = 1, max = 20, message = "店铺名称长度必须在1到20个字符之间")
    private String shopName;

    @Schema(description = "品牌名", example = "TechBrand")
    @Length(max = 20, message = "品牌名长度不能超过20个字符")
    private String brandName;

    @NotNull
    @Range(min = 100000, max = 999999, message = "验证码格式错误")
    @Schema(description = "验证码", requiredMode = Schema.RequiredMode.REQUIRED, example = "123456")
    private Integer captcha;
}
