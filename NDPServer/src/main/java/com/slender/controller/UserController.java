package com.slender.controller;

import com.slender.annotation.CheckAuthentication;
import com.slender.annotation.FakeInterface;
import com.slender.dto.authentication.LogoffRequest;
import com.slender.dto.user.UserResetRequest;
import com.slender.dto.user.UserUpdateRequest;
import com.slender.entity.User;
import com.slender.result.Response;
import com.slender.service.interfase.UserService;
import com.slender.vo.RefreshData;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@Tag(name = "用户接口")
@RequestMapping("/users")
@CheckAuthentication
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @FakeInterface
    @DeleteMapping("/{uid}/logout")
    @Operation(summary = "登出")
    public Response<Void> logout(@AuthenticationPrincipal Long uid,
                                 @PathVariable("uid") Long requestUid){
        return null;
    }

    @GetMapping("/{uid}")
    @Operation(summary = "获取用户信息")
    public Response<User> get(@AuthenticationPrincipal Long uid,
                              @PathVariable("uid") Long queryUid){
        return Response.success(userService.getById(uid));
    }

    @PutMapping("/{uid}")
    @Operation(summary = "更新用户信息")
    public Response<Void> update(@AuthenticationPrincipal Long authenticatedUid,
                                 @PathVariable("uid") Long requestUid,
                                 @RequestBody @Validated UserUpdateRequest updateRequest){
        userService.update(updateRequest,authenticatedUid);
        return Response.success("更新成功");
    }

    @PutMapping("/{uid}/reset")
    @Operation(summary = "重置重要信息")
    public Response<Void> reset(@AuthenticationPrincipal Long uid,
                                @PathVariable("uid") Long requestUid,
                                @RequestBody @Validated UserResetRequest userResetRequest){
        String message=userResetRequest.getType().getColumn();
        userService.reset(userResetRequest,uid);
        return Response.success("重置"+message+"成功");
    }

    @GetMapping("/{uid}/refresh")
    @Operation(summary = "刷新token")
    public Response<RefreshData> refresh(@AuthenticationPrincipal Long uid,
                                         @PathVariable("uid") Long requestUid){
        return Response.success("token刷新成功",userService.refresh(uid));
    }


    @DeleteMapping("/{uid}")
    @Operation(summary = "注销")
    public Response<Void> logoff(@AuthenticationPrincipal Long uid,
                                 @PathVariable("uid") Long requestUid,
                                 @Validated LogoffRequest request){
        userService.logoff(uid,request);
        return Response.success("注销成功");
    }

}
