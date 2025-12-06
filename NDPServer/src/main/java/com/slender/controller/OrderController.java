package com.slender.controller;

import com.slender.annotation.CheckAuthentication;
import com.slender.dto.order.OrderCreateRequest;
import com.slender.dto.order.OrderUpdateRequest;
import com.slender.entity.Order;
import com.slender.result.Response;
import com.slender.service.interfase.OrderService;
import com.slender.vo.ListData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CheckAuthentication
@Tag(name = "订单接口")
@RequestMapping("/users/{uid}/orders")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;

    @GetMapping("/{bid}")
    @Operation(summary = "获取单个用户订单")
    public Response<Order> get(@Parameter(hidden = true) @AuthenticationPrincipal Long uid,
                               @PathVariable("uid") Long requestUid,
                               @PathVariable Long bid) {
        return Response.success(orderService.get(bid));
    }

    @GetMapping
    @Operation(summary = "获取用户订单列表")
    public Response<ListData<Order>> getList(@AuthenticationPrincipal Long uid,
                                         @PathVariable("uid") Long requestUid,
                                         @RequestParam Boolean order) {
        return Response.success(orderService.getList(uid,order));
    }

    @PostMapping
    @Operation(summary = "创建用户订单")
    public Response<Void> add(@AuthenticationPrincipal Long uid,
                              @PathVariable("uid") Long requestUid,
                              @RequestBody @Validated OrderCreateRequest request) {
        orderService.create(uid,request);
        return Response.success("订单创建成功");
    }

    @PutMapping("/{bid}")
    @Operation(summary = "修改用户订单")
    public Response<Void> update(@AuthenticationPrincipal Long uid,
                                 @PathVariable("uid") Long requestUid,
                                 @PathVariable Long bid,
                                 @RequestBody @Validated OrderUpdateRequest request) {
        orderService.update(bid,request);
        return Response.success("订单修改成功");
    }

    @DeleteMapping("/{bid}")
    @Operation(summary = "删除用户订单")
    public Response<Void> delete(@AuthenticationPrincipal Long uid,
                                 @PathVariable("uid") Long requestUid,
                                 @PathVariable Long bid) {
        orderService.delete(bid);
        return Response.success("订单删除成功");
    }
}

