package com.slender.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.slender.constant.user.UserField;
import com.slender.dto.RegisterRequest;
import com.slender.dto.UserUpdateRequest;
import com.slender.entity.User;
import com.slender.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Slf4j
@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserMapper userMapper;

    public Optional<User> findByDataBaseColumn(String column,String value) {
        return Optional.ofNullable(userMapper.selectOne(new QueryWrapper<User>().eq(column, value)));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userMapper.selectOne(new QueryWrapper<User>().eq(UserField.EMAIL, email)));
    }

    public Optional<User> findByUid(Long uid) {
        return Optional.ofNullable(userMapper.selectOne(new QueryWrapper<User>().eq(UserField.UID, uid)));
    }

    public void add(RegisterRequest registerRequest) {
        userMapper.insert(new User(registerRequest));
    }

    public boolean updatePassword(String password, Long uid) {
        return userMapper.update(new UpdateWrapper<User>().eq(UserField.UID, uid).set(UserField.PASSWORD, password)) != 0;
    }

    public void update(UserUpdateRequest updateRequest, Long uid) {
        userMapper.update(new UpdateWrapper<User>()
                .eq(UserField.UID, uid)
                .set(updateRequest.getUserName()!=null,UserField.USER_NAME, updateRequest.getUserName())
                .set(updateRequest.getAge()!=null,UserField.AGE, updateRequest.getAge())
                .set(updateRequest.getGender()!=null,UserField.GENDER, updateRequest.getGender())
                .set(updateRequest.getBirthday()!=null,UserField.BIRTHDAY, updateRequest.getBirthday())
        );
    }
}
