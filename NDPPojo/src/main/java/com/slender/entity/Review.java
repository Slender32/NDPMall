package com.slender.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.slender.constant.other.EntityConstant;
import com.slender.dto.product.ProductCommentRequest;
import com.slender.enumeration.DeleteStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.hibernate.validator.constraints.Range;

import java.time.LocalDateTime;

@Data
@Schema(description = "商品评论")
@NoArgsConstructor
public class Review {

    @Schema(description = "评论ID", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    @TableId("rid")
    private Long rid;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "2001")
    @NotNull(message = "产品ID不能为空")
    @Positive(message = "产品ID必须为正整数")
    private Long pid;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "3001")
    @NotNull(message = "用户ID不能为空")
    @Positive(message = "用户ID必须为正整数")
    private Long uid;

    @Schema(description = "订单ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "4001")
    @NotNull(message = "订单ID不能为空")
    @Positive(message = "订单ID必须为正整数")
    private Long oid;

    @Schema(description = "评论时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "评论时间不得超前于当前时间")
    private LocalDateTime createTime;

    @Schema(description = "评论内容", requiredMode = Schema.RequiredMode.REQUIRED, example = "该用户给出五星好评！")
    @NotBlank(message = "评论内容不能为空")
    @Length(max = 800, message = "评论内容不能超过800个字符")
    private String comment;

    @Schema(description = "星级评分（1-5）", allowableValues = {"1", "2", "3", "4", "5"}, example = "5")
    @NotNull(message = "星级不能为空")
    @Range(min = 1, max = 5, message = "星级必须为1-5的整数")
    private Integer star;

    @Schema(description = "删除标记",
            allowableValues = {EntityConstant.Delete.DELETED, EntityConstant.Delete.NORMAL},
            accessMode = Schema.AccessMode.READ_ONLY)
    private DeleteStatus deleted;

    public Review(Long pid, Long uid, ProductCommentRequest request) {
        this.pid = pid;
        this.uid = uid;
        this.oid = request.getOid();
        this.createTime = LocalDateTime.now();
        this.comment = request.getComment();
        this.star = request.getStar();
    }
}
