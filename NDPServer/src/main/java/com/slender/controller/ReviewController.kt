package com.slender.controller

import com.slender.dto.product.ReviewAddRequest
import com.slender.dto.product.ReviewUpdateRequest
import com.slender.entity.Review
import com.slender.result.Response
import com.slender.service.interfase.ReviewService
import com.slender.vo.ListData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/products/{pid}/reviews")
@Tag(name = "评论接口")
open class ReviewController(
    private val reviewService: ReviewService
) {
    @PostMapping
    @Operation(summary = "评论产品")
    open fun addReview(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable pid: Long,
        @RequestBody @Validated request: ReviewAddRequest
    ): Response<Unit> {
        reviewService.comment(uid, pid, request)
        return Response.success("评论成功")
    }

    @DeleteMapping("/{rid}")
    @Operation(summary = "删除评论")
    open fun deleteReview(
        @PathVariable pid: Long,
        @PathVariable rid: Long
    ): Response<Unit> {
        reviewService.delete(rid)
        return Response.success("删除成功")
    }

    @PutMapping("/{rid}")
    @Operation(summary = "修改评论")
    open fun updateReview(
        @PathVariable pid: Long,
        @PathVariable rid: Long,
        @RequestBody @Validated request: ReviewUpdateRequest
    ): Response<Unit> {
        reviewService.update(rid, request)
        return Response.success("修改成功")
    }

    @GetMapping
    @Operation(summary = "获取产品评论列表")
    open fun getProductReviews(
        @PathVariable pid: Long
    ): Response<ListData<Review>> = Response.success(reviewService.getProductList(pid))

    @GetMapping("/user")
    @Operation(summary = "获取用户评论列表")
    open fun getUserReviews(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable pid: Long
    ): Response<ListData<Review>> =
        Response.success(reviewService.getUserList(uid))

    @GetMapping("/{rid}")
    @Operation(summary = "获取单条评论")
    open fun getReview(
        @PathVariable pid: Long,
        @PathVariable rid: Long
    ): Response<Review> =
        Response.success(reviewService.get(rid))
}