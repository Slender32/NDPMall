package com.slender.controller;

import com.slender.dto.authentication.LogoffRequest;
import com.slender.dto.merchant.MerchantRegisterRequest;
import com.slender.dto.user.MerchantUpdateRequest;
import com.slender.entity.Merchant;
import com.slender.result.Response;
import com.slender.service.interfase.MerchantService;
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
@RequestMapping("/merchants")
@RequiredArgsConstructor
@Tag(name = "商家接口")
public class MerchantController {
    private final MerchantService merchantService;

    @PostMapping("/register")
    @Operation(summary = "商家注册")
    public Response<Void> register(@AuthenticationPrincipal Long uid,
                                   @RequestBody @Validated MerchantRegisterRequest merchantRegisterRequest){
        merchantService.register(uid, merchantRegisterRequest);
        return Response.success("注册成功");
    }

    @GetMapping("/{mid}")
    @Operation(summary = "查询信息(Mid)")
    public Response<Merchant> getByMid(@AuthenticationPrincipal Long uid,
                                       @PathVariable Long mid){
        return Response.success(merchantService.getByMid(mid));
    }

    @GetMapping
    @Operation(summary = "查询信息(Uid)")
    public Response<Merchant> getByUid(@AuthenticationPrincipal Long uid){
        return Response.success(merchantService.getByUid(uid));
    }

    @DeleteMapping("/{mid}")
    @Operation(summary = "商家注销")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    public Response<Void> logoff(@AuthenticationPrincipal Long uid,
                                 @PathVariable Long mid,
                                 @Validated LogoffRequest request){
        merchantService.delete(uid,mid,request);
        return Response.success("注销成功");
    }

    @PutMapping("/{mid}")
    @Operation(summary = "修改商家信息")
    @PreAuthorize("hasAnyAuthority('MERCHANT','ADMINISTRATION')")
    public Response<Void> update(@AuthenticationPrincipal Long uid,
                                 @PathVariable Long mid,
                                 @RequestBody @Validated MerchantUpdateRequest request){
        merchantService.update(mid,request);
        return Response.success("更新信息成功");
    }
}
