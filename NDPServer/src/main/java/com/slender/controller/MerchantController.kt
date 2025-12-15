package com.slender.controller

import com.slender.dto.authentication.LogoffRequest
import com.slender.dto.merchant.MerchantRegisterRequest
import com.slender.dto.user.MerchantUpdateRequest
import com.slender.entity.Merchant
import com.slender.result.Response
import com.slender.service.interfase.MerchantService
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.access.prepost.PreAuthorize
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/merchants")
@Tag(name = "商家接口")
open class MerchantController(
    private val merchantService: MerchantService
) {
    @PostMapping("/register")
    @Operation(summary = "商家注册")
    open fun registerMerchant(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @RequestBody @Validated merchantRegisterRequest: MerchantRegisterRequest
    ): Response<Unit> {
        merchantService.register(uid, merchantRegisterRequest)
        return Response.success("注册成功")
    }

    @GetMapping("/{mid}")
    @Operation(summary = "查询信息(Mid)")
    open fun getByMid(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable mid: Long
    ): Response<Merchant> = Response.success(merchantService.getByMid(mid))

    @GetMapping
    @Operation(summary = "查询信息(Uid)")
    open fun getByUid(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long
    ): Response<Merchant> = Response.success(merchantService.getByUid(uid))


    @DeleteMapping("/{mid}")
    @Operation(summary = "商家注销")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    open fun logoffMerchant(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable mid: Long,
        @Validated request: LogoffRequest
    ): Response<Unit> {
        merchantService.delete(uid, mid, request)
        return Response.success("注销成功")
    }

    @PutMapping("/{mid}")
    @Operation(summary = "修改商家信息")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    open fun updateMerchant(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable mid: Long,
        @RequestBody @Validated request: MerchantUpdateRequest
    ): Response<Unit> {
        merchantService.update(mid, request)
        return Response.success("更新信息成功")
    }
}