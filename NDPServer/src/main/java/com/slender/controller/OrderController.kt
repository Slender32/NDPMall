package com.slender.controller

import com.slender.annotation.CheckAuthentication
import com.slender.dto.order.OrderCreateRequest
import com.slender.dto.order.OrderUpdateRequest
import com.slender.entity.Order
import com.slender.result.Response
import com.slender.service.interfase.OrderService
import com.slender.vo.ListData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@CheckAuthentication
@Tag(name = "订单接口")
@RequestMapping("/users/{uid}/orders")
open class OrderController(
    private val orderService: OrderService
) {
    @GetMapping("/{bid}")
    @Operation(summary = "获取单个用户订单")
    open fun getOrder(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @PathVariable bid: Long
    ): Response<Order> = Response.success(orderService.get(bid, uid))

    @GetMapping
    @Operation(summary = "获取用户订单列表")
    open fun getOrderList(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @RequestParam order: Boolean
    ): Response<ListData<Order>> = Response.success(orderService.getList(uid, order))

    @PostMapping
    @Operation(summary = "创建用户订单")
    open fun createOrder(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @RequestBody @Validated request: OrderCreateRequest
    ): Response<Unit> {
        orderService.create(uid, request)
        return Response.success("订单创建成功")
    }

    @PutMapping("/{bid}")
    @Operation(summary = "修改用户订单")
    open fun updateOrder(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @PathVariable bid: Long,
        @RequestBody @Validated request: OrderUpdateRequest
    ): Response<Unit> {
        orderService.update(bid, uid, request)
        return Response.success("订单修改成功")
    }

    @DeleteMapping("/{bid}")
    @Operation(summary = "删除用户订单")
    open fun deleteOrder(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @PathVariable bid: Long
    ): Response<Unit> {
        orderService.delete(bid)
        return Response.success("订单删除成功")
    }
}