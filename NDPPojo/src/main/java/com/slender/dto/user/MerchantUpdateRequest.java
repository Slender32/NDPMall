package com.slender.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
public class MerchantUpdateRequest {

    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不得超前于当前时间")
    private LocalDateTime createTime;

    @Schema(description = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "数码优选旗舰店")
    @Length(min = 1, max = 20, message = "店铺名称长度必须在1到20个字符之间")
    private String shopName;

    @Schema(description = "品牌名", example = "TechBrand")
    @Length(max = 20, message = "品牌名长度不能超过20个字符")
    private String brandName;
}
