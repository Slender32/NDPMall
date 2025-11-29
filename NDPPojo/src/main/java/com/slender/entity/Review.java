package com.slender.entity;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Schema(description = "商品评论")
public class Review {

    @Schema(description = "评论ID", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Integer rid;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2001")
    @NotNull(message = "产品ID不能为空")
    @Positive(message = "产品ID必须为正整数")
    private Integer pid;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3001")
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正整数")
    private Integer uid;

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4001")
    @NotNull(message = "订单ID不能为空")
    @Positive(message = "订单ID必须为正整数")
    private Integer oid;

    @Schema(description = "评论时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "评论时间不得超前于当前时间")
    private LocalDateTime createTime;

    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "该用户给出五星好评！")
    @NotBlank(message = "评论内容不能为空")
    @Size(max = 800, message = "评论内容不能超过800个字符")
    private String comment;

    @Schema(description = "星级评分（1-5）", allowableValues = {"1", "2", "3", "4", "5"}, example = "5")
    @NotNull(message = "星级不能为空")
    @Min(value = 1, message = "星级最低为1")
    @Max(value = 5, message = "星级最高为5")
    private Integer star;
}
