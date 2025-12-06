package com.slender.controller;

import com.slender.annotation.CheckAuthentication;
import com.slender.dto.user.FavouriteRequest;
import com.slender.entity.Product;
import com.slender.result.Response;
import com.slender.service.interfase.FavouriteService;
import com.slender.vo.ListData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "用户接口")
@RequestMapping("/users/{uid}/favorite")
@CheckAuthentication
@RequiredArgsConstructor
public class FavoriteController {
    private final FavouriteService favouriteService;
    @GetMapping
    @Operation(summary = "获取用户收藏产品")
    public Response<ListData<Product>> get(@AuthenticationPrincipal Long uid,
                                           @PathVariable("uid") Long requestUid){
        return Response.success(favouriteService.getList(uid));
    }
    @PostMapping
    @Operation(summary = "收藏产品")
    public Response<Void> add(@AuthenticationPrincipal Long uid,
                              @PathVariable("uid") Long requestUid,
                              @RequestBody @Validated FavouriteRequest request){
        favouriteService.add(uid, request);
        return Response.success("收藏成功");
    }

    @DeleteMapping("/{fid}")
    @Operation(summary = "取消收藏产品")
    public Response<Void> delete(@AuthenticationPrincipal Long uid,
                                 @PathVariable("uid") Long requestUid,
                                 @PathVariable Long fid){
        favouriteService.delete(fid);
        return Response.success("取消收藏成功");
    }
}
