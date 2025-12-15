package com.slender.controller

import com.slender.dto.product.CategoryAddRequest
import com.slender.dto.product.CategoryUpdateRequest
import com.slender.entity.Category
import com.slender.result.Response
import com.slender.service.interfase.CategoryService
import com.slender.vo.ListData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/categories")
@Tag(name = "分类接口")
open class CategoryController(
    private val categoryService: CategoryService
) {
    @GetMapping
    @Operation(summary = "获取分类列表")
    open fun getCategoryList(
        @RequestParam order: Boolean
    ): Response<ListData<Category>> = Response.success(categoryService.getList(order))


    @GetMapping("/{cid}")
    @Operation(summary = "获取分类")
    open fun getCategory(
        @PathVariable cid: Long
    ): Response<Category> = Response.success(categoryService.get(cid))


    @PostMapping
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    @Operation(summary = "增加分类")
    open fun addCategory(
        @RequestBody @Validated request: CategoryAddRequest
    ): Response<Unit> {
        categoryService.add(request)
        return Response.success("添加成功")
    }

    @DeleteMapping("/{cid}")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    @Operation(summary = "删除分类")
    open fun deleteCategory(
        @PathVariable cid: Long
    ): Response<Unit> {
        categoryService.delete(cid)
        return Response.success("删除成功")
    }

    @PutMapping("/{cid}")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    @Operation(summary = "更新分类")
    open fun updateCategory(
        @PathVariable cid: Long,
        @RequestBody @Validated request: CategoryUpdateRequest
    ): Response<Unit> {
        categoryService.update(cid, request)
        return Response.success("更新成功")
    }
}