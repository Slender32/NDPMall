package com.slender.entity;

import com.baomidou.mybatisplus.annotation.TableId;
import com.slender.constant.other.EntityConstant;
import com.slender.dto.user.FavouriteRequest;
import com.slender.enumeration.DeleteStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@Schema(description = "收藏信息")
public class Favourite {

    @Schema(description = "收藏ID", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    @TableId("fid")
    private Long fid;

    @Schema(description = "用户ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "101")
    @NotNull(message = "用户ID不能为空")
    private Long uid;

    @Schema(description = "产品ID", requiredMode = Schema.RequiredMode.REQUIRED, example = "201")
    @NotNull(message = "产品ID不能为空")
    private Long pid;

    @Schema(description = "创建时间", accessMode = Schema.AccessMode.READ_ONLY)
    @PastOrPresent(message = "创建时间不合法")
    private LocalDateTime createTime;

    @Schema(description = "删除标记",
            allowableValues = {EntityConstant.Delete.DELETED, EntityConstant.Delete.NORMAL},
            accessMode = Schema.AccessMode.READ_ONLY)
    private DeleteStatus deleted;

    public Favourite(Long uid, FavouriteRequest request) {
        this.uid = uid;
        this.pid = request.getPid();
        this.createTime = LocalDateTime.now();
    }
}

