package com.slender.controller

import com.slender.annotation.CheckAuthentication
import com.slender.dto.user.AddressAddRequest
import com.slender.dto.user.AddressUpdateRequest
import com.slender.entity.Address
import com.slender.result.Response
import com.slender.service.interfase.AddressService
import com.slender.vo.ListData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.web.bind.annotation.*

@RestController
@CheckAuthentication
@Tag(name = "用户地址接口")
@RequestMapping("/users/{uid}/addresses")
open class AddressController(
    private val addressService: AddressService
) {
    @GetMapping("/{aid}")
    @Operation(summary = "获取单个用户地址")
    open fun getAddress(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @PathVariable aid: Long
    ): Response<Address> = Response.success("地址获取成功", addressService.get(aid))

    @GetMapping
    @Operation(summary = "获取用户地址")
    open fun getAddressList(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @RequestParam order: Boolean
    ): Response<ListData<Address>> = Response.success(addressService.getList(uid, order))

    @PostMapping
    @Operation(summary = "增加用户地址")
    open fun addAddress(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @RequestBody request: AddressAddRequest
    ): Response<Unit> {
        addressService.add(uid, request)
        return Response.success("地址添加成功")
    }

    @PutMapping("/{aid}")
    @Operation(summary = "修改用户地址")
    open fun updateAddress(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @PathVariable aid: Long,
        @RequestBody request: AddressUpdateRequest
    ): Response<Unit> {
        addressService.update(uid, request)
        return Response.success("地址修改成功")
    }

    @DeleteMapping("/{aid}")
    @Operation(summary = "删除用户地址")
    open fun deleteAddress(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @PathVariable aid: Long
    ): Response<ListData<Address>> {
        addressService.delete(aid)
        return Response.success("删除成功")
    }
}