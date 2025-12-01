package com.slender.controller;

import com.slender.dto.product.ProductCommentRequest;
import com.slender.dto.product.ProductListRequest;
import com.slender.entity.Product;
import com.slender.result.Response;
import com.slender.service.interfase.ProductService;
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

    @PostMapping("/{pid}")
    @Operation(summary = "评论产品")
    public Response<Void> comment(@AuthenticationPrincipal Long uid,
                                  @PathVariable Long pid,
                                  @RequestBody @Validated ProductCommentRequest request){
        productService.comment(uid, pid, request);
        return Response.success("评论成功");
    }

    @PostMapping
    @Operation(summary = "上架产品")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    public Response<Void> list(@AuthenticationPrincipal Long uid,
                               @RequestBody @Validated ProductListRequest request) {
        productService.list(uid,request);
        return Response.success("上架成功");
    }

}
