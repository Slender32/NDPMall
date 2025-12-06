package com.slender.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.slender.constant.other.EntityConstant;
import com.slender.dto.product.CategoryAddRequest;
import com.slender.enumeration.DeleteStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.validator.constraints.Length;

import java.time.LocalDateTime;

@Data
@Schema(description = "商品分类")
@NoArgsConstructor
public class Category {

    @Schema(description = "分类ID", example = "101", accessMode = Schema.AccessMode.READ_ONLY)
    @TableId("cid")
    private Long cid;

    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不得超前于当前时间")
    private LocalDateTime createTime;

    @Schema(description = "更新时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "更新时间不得超前于当前时间")
    private LocalDateTime updateTime;

    @Schema(description = "分类名称", requiredMode = Schema.RequiredMode.REQUIRED, example = "手机数码")
    @NotBlank(message = "分类名称不能为空")
    @Length(min = 1, max = 20, message = "分类名称长度必须在1到20个字符之间")
    private String kindName;

    @Schema(description = "删除标记",
            allowableValues = {EntityConstant.Delete.DELETED, EntityConstant.Delete.NORMAL},
            accessMode = Schema.AccessMode.READ_ONLY)
    private DeleteStatus deleted;

    public Category(CategoryAddRequest request) {
        this.kindName=request.getKindName();
        this.createTime=LocalDateTime.now();
        this.updateTime=LocalDateTime.now();
    }
}
