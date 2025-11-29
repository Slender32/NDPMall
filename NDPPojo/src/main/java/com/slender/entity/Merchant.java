package com.slender.entity;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "商家信息")
public class Merchant {

    @Schema(description = "商家ID", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer mid;

    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不得超前于当前时间")
    private LocalDateTime createTime;

    @Schema(description = "修改时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "更新时间不得超前于当前时间")
    private LocalDateTime updateTime;

    @Schema(description = "归属的用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2001")
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正整数")
    private Integer uid;

    @Schema(description = "店铺名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "数码优选旗舰店")
    @NotBlank(message = "店铺名称不能为空")
    @Size(min = 1, max = 20, message = "店铺名称长度必须在1到20个字符之间")
    private String shopName;

    @Schema(description = "品牌名", example = "TechBrand")
    @Size(max = 20, message = "品牌名长度不能超过20个字符")
    private String brandName;
}
