package com.slender.dto.product;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

@Data
public class ReviewUpdateRequest {
    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "该用户给出五星好评！")
    @Length(max = 800, message = "评论内容不能超过800个字符")
    private String comment;

    @Schema(description = "星级评分（1-5）", allowableValues = {"1", "2", "3", "4", "5"}, example = "5")
    @Range(min = 1, max = 5, message = "星级应为1-5")
    private Integer star;
}
