package com.slender.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import java.time.LocalDateTime;

@Data
@Schema(description = "收货地址信息")
public class Address {

    @Schema(description = "收货地址ID", example = "1001")
    private Integer aid;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "12345")
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正整数")
    private Integer uid;

    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不得超前于当前时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "更新时间不得超前于当前时间")
    private LocalDateTime updateTime;

    @Schema(description = "省份", requiredMode = Schema.RequiredMode.REQUIRED, example = "广东省")
    @NotBlank(message = "省份不能为空")
    @Size(max = 10, message = "省份名称不能超过10个字符")
    private String province;

    @Schema(description = "城市", requiredMode = Schema.RequiredMode.REQUIRED, example = "深圳市")
    @NotBlank(message = "城市不能为空")
    @Size(max = 10, message = "城市名称不能超过10个字符")
    private String city;

    @Schema(description = "详细地址（街道、门牌号等）", example = "南山区科技园科兴科学园A3栋")
    @Size(max = 255, message = "详细地址长度不能超过255个字符")
    private String detail;
}
