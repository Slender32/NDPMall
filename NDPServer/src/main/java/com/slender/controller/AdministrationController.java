package com.slender.controller;

import com.slender.result.Response;
import com.slender.service.interfase.UserService;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@Tag(name = "管理员接口")
@RequestMapping("/admins")
@RequiredArgsConstructor
public class AdministrationController {
    private final UserService userService;
    @DeleteMapping("/block/{uid}")
    public Response<Void> block(@AuthenticationPrincipal Long uid,
                                @PathVariable("uid") Long userUid){
        userService.block(userUid);
        return Response.success("封禁成功");
    }
}
