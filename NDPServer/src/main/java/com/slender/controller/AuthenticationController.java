package com.slender.controller;

import com.slender.dto.*;
import com.slender.entity.User;
import com.slender.result.Response;
import com.slender.service.interfase.UserService;
import com.slender.vo.LoginData;
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
@RequestMapping("/auth")
@RequiredArgsConstructor
@Tag(name = "访问控制",description = "用户登录登出刷新接口")
public class AuthenticationController {
    private final UserService userService;
    @PostMapping("/login/password")
    @Operation(summary = "密码登录")
    public Response<LoginData> loginByPassword(@RequestBody LoginByPasswordRequest request){
        return null;
    }

    @PostMapping("/login/captcha")
    @Operation(summary = "验证码登录")
    public Response<LoginData> loginByCaptcha(@RequestBody LoginByCaptchaRequest request){
        return null;
    }

    @PostMapping("/captcha")
    @Operation(summary = "验证码获取")
    public Response<Void> loginByCaptcha(@RequestBody @Validated CaptchaRequest captchaRequest){
        userService.sendCaptcha(captchaRequest);
        return Response.success("验证码发送成功");
    }

    @PostMapping("/register")
    @Operation(summary = "注册")
    public Response<Void> register(@RequestBody @Validated RegisterRequest registerRequest){
        userService.register(registerRequest);
        return Response.success("注册成功");
    }

    @PostMapping("/reset")
    @Operation(summary = "重置密码")
    public Response<Void> reset(@RequestBody @Validated ResetRequest resetRequest, @AuthenticationPrincipal User user){
        return userService.reset(resetRequest,user) ?
                Response.success("重置密码成功") :
                Response.fail("重置密码失败");
    }

    @GetMapping("/refresh")
    @Operation(summary = "重置密码")
    public Response<RefreshData> refresh(@AuthenticationPrincipal Long uid){
        return Response.success("token刷新成功",userService.refresh(uid));
    }
}
