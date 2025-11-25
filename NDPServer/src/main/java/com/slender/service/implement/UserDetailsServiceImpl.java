package com.slender.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.slender.entity.User;
import com.slender.mapper.UserMapper;
import com.slender.model.LoginUserDetail;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class AuthServiceImpl implements UserDetailsService{

    private final UserMapper userMapper;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        log.info("用户名: {}", username);
        final User user = userMapper.selectOne(new QueryWrapper<User>().eq("user_name", username));
        if(user == null) {
            log.error("用户不存在: {}", username);
            throw new UsernameNotFoundException("用户不存在");
        }
        System.out.println(bCryptPasswordEncoder.matches("123456xyz", user.getPassword()));
        return new LoginUserDetail(user.getUid(), user.getUserName(), user.getPassword());
    }
}
