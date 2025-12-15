package com.slender.controller

import com.slender.annotation.FakeInterface
import com.slender.dto.authentication.CaptchaRequest
import com.slender.dto.authentication.LoginByCaptchaRequest
import com.slender.dto.authentication.LoginByPasswordRequest
import com.slender.dto.user.UserRegisterRequest
import com.slender.result.Response
import com.slender.service.interfase.UserService
import com.slender.vo.LoginData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/auth")
@Tag(name = "访问控制", description = "用户登录登出刷新接口")
open class AuthenticationController(
    private val userService: UserService
) {
    @FakeInterface
    @PostMapping("/login/password")
    @Operation(summary = "密码登录")
    open fun loginByPassword(
        @RequestBody request: LoginByPasswordRequest
    ): Response<LoginData> = null!!


    @FakeInterface
    @PostMapping("/login/captcha")
    @Operation(summary = "验证码登录")
    open fun loginByCaptcha(
        @RequestBody request: LoginByCaptchaRequest
    ): Response<LoginData> = null!!

    
    @PostMapping("/captcha")
    @Operation(summary = "验证码获取")
    open fun getCaptcha(
        @RequestBody @Validated captchaRequest: CaptchaRequest
    ): Response<Unit> {
        userService.sendCaptcha(captchaRequest)
        return Response.success("验证码发送成功")
    }
    
    @PostMapping("/register")
    @Operation(summary = "注册")
    open fun register(
        @RequestBody @Validated userRegisterRequest: UserRegisterRequest
    ): Response<Unit> {
        userService.register(userRegisterRequest)
        return Response.success("注册成功")
    }
}