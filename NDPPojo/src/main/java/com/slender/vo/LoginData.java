package com.slender.vo;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "登录返回结果")
public record LoginData(
     @Schema(description = "用户ID", example = "100")
     Long uid,

     @Schema(description = "用户名", example = "slender")
     String userName,

     @Schema(description = "访问令牌(JWT)")
     String accessToken,

     @Schema(description = "刷新令牌(JWT)")
     String refreshToken
){}

