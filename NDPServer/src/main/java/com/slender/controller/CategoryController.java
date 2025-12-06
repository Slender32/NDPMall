package com.slender.controller;

import com.slender.dto.product.CategoryAddRequest;
import com.slender.dto.product.CategoryUpdateRequest;
import com.slender.entity.Category;
import com.slender.result.Response;
import com.slender.service.interfase.CategoryService;
import com.slender.vo.ListData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Tag(name = "分类接口")
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Operation(summary = "获取分类列表")
    public Response<ListData<Category>> get(@RequestParam Boolean order) {
        return Response.success(categoryService.getList(order));
    }

    @GetMapping("/{cid}")
    @Operation(summary = "获取分类")
    public Response<Category> get(@PathVariable Long cid) {
        return Response.success(categoryService.get(cid));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    @Operation(summary = "增加分类")
    public Response<Void> add(@RequestBody @Validated CategoryAddRequest request) {
        categoryService.add(request);
        return Response.success("添加成功");
    }

    @DeleteMapping("/{cid}")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    @Operation(summary = "删除分类")
    public Response<Void> delete(@PathVariable Long cid) {
        categoryService.delete(cid);
        return Response.success("删除成功");
    }

    @PutMapping("/{cid}")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    @Operation(summary = "更新分类")
    public Response<Void> update(@PathVariable Long cid, @RequestBody @Validated CategoryUpdateRequest request) {
        categoryService.update(cid, request);
        return Response.success("更新成功");
    }
}
