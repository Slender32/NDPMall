package com.slender.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
public class CategoryAddRequest {
    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "手机数码")
    @NotBlank(message = "分类名称不能为空")
    @Length(min = 1, max = 20, message = "分类名称长度必须在1到20个字符之间")
    private String kindName;
}
