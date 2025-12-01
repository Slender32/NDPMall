package com.slender.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import org.hibernate.validator.constraints.Range;

@Data
public class ProductCommentRequest {
    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "该用户给出五星好评！")
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 800, message = "评论内容不能超过800个字符")
    private String comment;

    @Schema(description = "星级评分（1-5）", allowableValues = {"1", "2", "3", "4", "5"}, example = "5")
    @NotNull(message = "星级不能为空")
    @Range(min = 1, max = 5, message = "星级应为1-5")
    private Integer star;

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4001")
    @NotNull(message = "没有订单无法评论")
    @Positive(message = "订单ID必须为正整数")
    private Long oid;
}
