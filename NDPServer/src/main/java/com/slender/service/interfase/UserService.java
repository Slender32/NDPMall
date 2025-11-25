package com.slender.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.LoginRequest;
import com.slender.entity.User;
import com.slender.vo.LoginData;

import java.util.Optional;

public interface UserService extends IService<User> {
    Optional<LoginData> login(LoginRequest loginRequest);
}
