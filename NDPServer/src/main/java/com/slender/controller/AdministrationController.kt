package com.slender.controller

import com.slender.result.Response
import com.slender.service.interfase.OrderService
import com.slender.service.interfase.ProductService
import com.slender.service.interfase.UserService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@Tag(name = "管理员接口")
@RequestMapping("/admins")
open class AdministrationController(
    private val userService: UserService,
    private val orderService: OrderService,
    private val productService: ProductService
) {
    @DeleteMapping("/block/{uid}")
    @Operation(summary = "封禁用户")
    open fun block(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") userUid: Long
    ): Response<Unit> {
        userService.block(userUid)
        return Response.success("封禁成功")
    }

    @GetMapping("/orders/export")
    @Operation(summary = "导出订单")
    open fun exportOrders(response: HttpServletResponse) {
        orderService.export(response)
    }

    @GetMapping("/products/export")
    @Operation(summary = "导出产品")
    open fun exportProducts(response: HttpServletResponse) {
        productService.export(response)
    }
}