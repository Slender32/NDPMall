package com.slender.controller

import com.slender.annotation.CheckAuthentication
import com.slender.dto.user.FavouriteRequest
import com.slender.entity.Product
import com.slender.result.Response
import com.slender.service.interfase.FavouriteService
import com.slender.vo.ListData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "用户接口")
@RequestMapping("/users/{uid}/favorite")
@CheckAuthentication
open class FavoriteController(
    private val favouriteService: FavouriteService
) {
    @GetMapping
    @Operation(summary = "获取用户收藏产品")
    open fun getFavorites(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long
    ): Response<ListData<Product>> = Response.success(favouriteService.getList(uid))

    @PostMapping
    @Operation(summary = "收藏产品")
    open fun addFavorite(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @RequestBody @Validated request: FavouriteRequest
    ): Response<Unit> {
        favouriteService.add(uid, request)
        return Response.success("收藏成功")
    }

    @DeleteMapping("/{fid}")
    @Operation(summary = "取消收藏产品")
    open fun deleteFavorite(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @PathVariable fid: Long
    ): Response<Unit> {
        favouriteService.delete(fid)
        return Response.success("取消收藏成功")
    }
}