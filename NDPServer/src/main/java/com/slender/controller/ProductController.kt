package com.slender.controller

import com.slender.dto.product.ProductAddRequest
import com.slender.dto.product.ProductUpdateRequest
import com.slender.dto.product.ProductPageRequest
import com.slender.entity.Product
import com.slender.result.Response
import com.slender.service.interfase.ProductService
import com.slender.vo.FileData
import com.slender.vo.PageData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/products")
@Tag(name = "产品接口")
open class ProductController(
    private val productService: ProductService
) {
    @PutMapping("/{pid}")
    @Operation(summary = "更新产品")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    open fun updateProduct(
        @PathVariable pid: Long,
        @RequestBody @Validated request: ProductUpdateRequest
    ): Response<Unit> {
        productService.update(pid, request)
        return Response.success("更新成功")
    }

    @PostMapping("/{pid}/image")
    @Operation(summary = "上传产品图片")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    open fun uploadImage(
        @PathVariable pid: Long,
        @RequestParam file: MultipartFile
    ): Response<FileData> = Response.success(productService.updateImage(pid, file))

    @GetMapping("/{pid}")
    @Operation(summary = "获取产品详情")
    open fun getProduct(
        @PathVariable pid: Long
    ): Response<Product> = Response.success(productService.get(pid))

    @GetMapping
    @Operation(summary = "获取产品列表")
    open fun getProductPage(
        @Validated request: ProductPageRequest
    ): Response<PageData<Product>> = Response.success(productService.get(request))

    @PostMapping
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    @Operation(summary = "添加产品")
    open fun addProduct(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @RequestBody @Validated request: ProductAddRequest
    ): Response<Unit> {
        productService.add(uid, request)
        return Response.success("添加成功")
    }

    @DeleteMapping("/{pid}")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    @Operation(summary = "删除产品")
    open fun deleteProduct(@PathVariable pid: Long): Response<Unit> {
        productService.delete(pid)
        return Response.success("删除成功")
    }
}