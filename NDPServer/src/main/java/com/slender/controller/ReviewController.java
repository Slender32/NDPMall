package com.slender.controller;

import com.slender.dto.product.ReviewAddRequest;
import com.slender.dto.product.ReviewUpdateRequest;
import com.slender.entity.Review;
import com.slender.result.Response;
import com.slender.service.interfase.ReviewService;
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
@RequestMapping("/products/{pid}/reviews")
@RequiredArgsConstructor
@Tag(name = "评论接口")
public class ReviewController {
    private final ReviewService reviewService;
    @PostMapping
    @Operation(summary = "评论产品")
    public Response<Void> comment(@AuthenticationPrincipal Long uid,
                                  @PathVariable Long pid,
                                  @RequestBody @Validated ReviewAddRequest request){
        reviewService.comment(uid, pid, request);
        return Response.success("评论成功");
    }

    @Operation(summary = "删除评论")
    @DeleteMapping("/{rid}")
    public Response<Void> delete(@PathVariable Long pid,
                                 @PathVariable Long rid){
        reviewService.delete(rid);
        return Response.success("删除成功");
    }

    @Operation(summary = "修改评论")
    @PutMapping("/{rid}")
    public Response<Void> update(@PathVariable Long pid,
                                 @PathVariable Long rid,
                                 @RequestBody @Validated ReviewUpdateRequest request){
        reviewService.update(rid,request);
        return Response.success("修改成功");
    }

    @GetMapping
    @Operation(summary = "获取产品评论列表")
    public Response<ListData<Review>> getList(@PathVariable Long pid){
        return Response.success(reviewService.getProductList(pid));
    }

    @GetMapping("/user")
    @Operation(summary = "获取用户评论列表")
    public Response<ListData<Review>> getList(@AuthenticationPrincipal Long uid,@PathVariable Long pid){
        return Response.success(reviewService.getUserList(uid));
    }

    @GetMapping("/{rid}")
    @Operation(summary = "获取单条评论")
    public Response<Review> get(@PathVariable Long pid,
                               @PathVariable Long rid){
        return Response.success(reviewService.get(rid));
    }
}
