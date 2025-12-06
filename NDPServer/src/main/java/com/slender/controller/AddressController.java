package com.slender.controller;

import com.slender.annotation.CheckAuthentication;
import com.slender.dto.user.AddressAddRequest;
import com.slender.dto.user.AddressUpdateRequest;
import com.slender.entity.Address;
import com.slender.result.Response;
import com.slender.service.interfase.AddressService;
import com.slender.vo.ListData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@CheckAuthentication
@Tag(name = "用户地址接口")
@RequestMapping("/users/{uid}/addresses")
@RequiredArgsConstructor
public class AddressController {
    private final AddressService addressService;

    @GetMapping("/{aid}")
    @Operation(summary = "获取单个用户地址")
    public Response<Address> get(@AuthenticationPrincipal Long uid,
                                 @PathVariable("uid") Long requestUid,
                                 @PathVariable Long aid){
        return Response.success("地址获取成功",addressService.get(aid));
    }

    @GetMapping
    @Operation(summary = "获取用户地址")
    public Response<ListData<Address>> get(@AuthenticationPrincipal Long uid,
                                           @PathVariable("uid") Long requestUid,
                                           @RequestParam Boolean order){
        return Response.success(addressService.getList(uid, order));
    }

    @PostMapping
    @Operation(summary = "增加用户地址")
    public Response<Void> add(@AuthenticationPrincipal Long uid,
                              @PathVariable("uid") Long requestUid,
                              @RequestBody AddressAddRequest request){
        addressService.add(uid,request);
        return Response.success("地址添加成功");
    }

    @PutMapping("/{aid}")
    @Operation(summary = "修改用户地址")
    public Response<Void> update(@AuthenticationPrincipal Long uid,
                                 @PathVariable("uid") Long requestUid,
                                 @PathVariable Long aid,
                                 @RequestBody AddressUpdateRequest request){
        addressService.update(uid,request);
        return Response.success("地址修改成功");
    }

    @DeleteMapping("/{aid}")
    @Operation(summary = "删除用户地址")
    public Response<ListData<Address>> delete(@AuthenticationPrincipal Long uid,
                                                  @PathVariable("uid") Long requestUid,
                                                  @PathVariable Long aid){
        addressService.delete(aid);
        return Response.success("删除成功");
    }
}
