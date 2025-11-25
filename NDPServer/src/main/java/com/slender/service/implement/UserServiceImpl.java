package com.slender.service;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.dto.LoginRequest;
import com.slender.entity.User;
import com.slender.mapper.UserMapper;
import com.slender.model.LoginUserDetail;
import com.slender.vo.LoginData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final AuthenticationManager authManager;
    @Override
    public Optional<LoginData> login(LoginRequest loginRequest) {
        log.info("用户登录：{}", loginRequest);
        final Authentication authenticate = authManager
                .authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getUserName(), loginRequest.getPassword()));
        if(authenticate != null){
            log.info("用户登录成功：{}", authenticate);
            final LoginUserDetail userData = ((LoginUserDetail) authenticate.getPrincipal());
            return Optional.of(new LoginData(userData.uid(), userData.userName(), "act","ret"));
        }else return Optional.empty();
    }
}
