package com.slender.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class AddressAddRequest {

    @Schema(description = "省份", requiredMode = Schema.RequiredMode.REQUIRED, example = "广东省")
    @NotBlank(message = "省份不能为空")
    @Length(max = 10, message = "省份名称不能超过10个字符")
    private String province;

    @Schema(description = "城市", requiredMode = Schema.RequiredMode.REQUIRED, example = "深圳市")
    @NotBlank(message = "城市不能为空")
    @Length(max = 10, message = "城市名称不能超过10个字符")
    private String city;

    @Schema(description = "详细地址（街道、门牌号等）", example = "南山区科技园科兴科学园A3栋")
    @Length(max = 255, message = "详细地址长度不能超过255个字符")
    private String detail;
}
