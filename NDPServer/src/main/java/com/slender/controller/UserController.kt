package com.slender.controller

import com.slender.annotation.CheckAuthentication
import com.slender.annotation.FakeInterface
import com.slender.dto.authentication.LogoffRequest
import com.slender.dto.user.UserResetRequest
import com.slender.dto.user.UserUpdateRequest
import com.slender.entity.User
import com.slender.result.Response
import com.slender.service.interfase.UserService
import com.slender.vo.FileData
import com.slender.vo.RefreshData
import io.swagger.v3.oas.annotations.Operation
import io.swagger.v3.oas.annotations.Parameter
import io.swagger.v3.oas.annotations.tags.Tag
import org.springframework.security.core.annotation.AuthenticationPrincipal
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.*
import org.springframework.web.multipart.MultipartFile

@RestController
@Tag(name = "用户接口")
@RequestMapping("/users/{uid}")
@CheckAuthentication
open class UserController(
    private val userService: UserService
) {

    @FakeInterface
    @DeleteMapping("/logout")
    @Operation(summary = "登出")
    open fun logout(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long
    ): Response<Unit> = null!!

    @GetMapping
    @Operation(summary = "获取用户信息")
    open fun getUserInfo(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") queryUid: Long
    ): Response<User> {
        val user = userService.getById(uid)
        user.password = null
        return Response.success(user)
    }

    @PutMapping
    @Operation(summary = "更新用户信息")
    open fun updateUser(
        @Parameter(hidden = true) @AuthenticationPrincipal authenticatedUid: Long,
        @PathVariable("uid") requestUid: Long,
        @RequestBody @Validated updateRequest: UserUpdateRequest
    ): Response<Unit> {
        userService.update(updateRequest, authenticatedUid)
        return Response.success("更新成功")
    }

    @PutMapping("/reset")
    @Operation(summary = "修改重要信息")
    open fun resetUserInfo(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @RequestBody @Validated userResetRequest: UserResetRequest
    ): Response<Unit> {
        val message = userResetRequest.type.column
        userService.reset(userResetRequest, uid)
        return Response.success("重置${message}成功")
    }

    @GetMapping("/refresh")
    @Operation(summary = "刷新令牌")
    open fun refreshToken(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long
    ): Response<RefreshData> =
        Response.success("token刷新成功", userService.refresh(uid))

    @DeleteMapping
    @Operation(summary = "注销")
    open fun logoffUser(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @Validated request: LogoffRequest
    ): Response<Unit> {
        userService.logoff(uid, request)
        return Response.success("注销成功")
    }

    @PostMapping("/image")
    @Operation(summary = "上传头像")
    open fun uploadAvatar(
        @Parameter(hidden = true) @AuthenticationPrincipal uid: Long,
        @PathVariable("uid") requestUid: Long,
        @RequestParam file: MultipartFile
    ): Response<FileData> =
        Response.success(userService.uploadAvatar(file))
}