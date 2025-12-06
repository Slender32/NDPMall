package com.slender.controller;

import com.slender.dto.product.ProductAddRequest;
import com.slender.dto.product.ProductUpdateRequest;
import com.slender.dto.product.ProductPageRequest;
import com.slender.entity.Product;
import com.slender.result.Response;
import com.slender.service.interfase.ProductService;
import com.slender.vo.PageData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
@Tag(name = "产品接口")
public class ProductController {
    private final ProductService productService;

    @PutMapping("/{pid}")
    @Operation(summary = "更新产品")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    public Response<Void> update(@PathVariable Long pid,
                                 @RequestBody @Validated ProductUpdateRequest request) {
        productService.update(pid,request);
        return Response.success("更新成功");
    }

    @GetMapping("/{pid}")
    @Operation(summary = "获取产品详情")
    public Response<Product> get(@PathVariable Long pid) {
        return Response.success(productService.get(pid));
    }

    @GetMapping
    @Operation(summary = "获取产品列表")
    public Response<PageData<Product>> page(@Validated ProductPageRequest request) {
        return Response.success(productService.get(request));
    }

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    @Operation(summary = "添加产品")
    public Response<Void> add(@AuthenticationPrincipal Long uid,
                             @RequestBody @Validated ProductAddRequest request) {
        productService.add(uid, request);
        return Response.success("添加成功");
    }

    @DeleteMapping("/{pid}")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    @Operation(summary = "删除产品")
    public Response<Void> delete(@PathVariable Long pid) {
        productService.delete(pid);
        return Response.success("删除成功");
    }
}
