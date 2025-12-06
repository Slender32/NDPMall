package com.slender.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
public class CategoryUpdateRequest {
    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不得超前于当前时间")
    private LocalDateTime createTime;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "手机数码")
    @Length(min = 1, max = 20, message = "分类名称长度必须在1到20个字符之间")
    private String kindName;
}
