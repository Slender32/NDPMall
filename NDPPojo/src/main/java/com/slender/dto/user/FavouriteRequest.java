package com.slender.dto.user;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;

@Data
public class FavouriteRequest {
    @Schema(description = "产品ID", example = "1001", accessMode = Schema.AccessMode.READ_ONLY)
    private Long pid;
}
