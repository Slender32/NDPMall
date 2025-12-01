package com.slender.controller;

import com.slender.constant.user.UserConstant;
import com.slender.dto.user.UserUpdateRequest;
import com.slender.result.Response;
import com.slender.service.interfase.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    @PostMapping("/{uid}")
    public Response<Void> update(@AuthenticationPrincipal Long authenticatedUid,
                                 @PathVariable("uid") Long requestUid,
                                 @RequestBody @Validated UserUpdateRequest updateRequest
    ){
        if(!Objects.equals(authenticatedUid, requestUid))
            return Response.fail(UserConstant.Message.VALIDATION_ERROR);
        userService.update(updateRequest,authenticatedUid);
        return Response.success("更新成功");
    }
}
