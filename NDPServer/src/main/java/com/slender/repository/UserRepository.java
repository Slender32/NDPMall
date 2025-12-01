package com.slender.repository;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.slender.constant.user.UserColumn;
import com.slender.constant.user.UserConstant;
import com.slender.dto.user.UserRegisterRequest;
import com.slender.dto.user.UserUpdateRequest;
import com.slender.entity.User;
import com.slender.mapper.UserMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserRepository {
    private final UserMapper userMapper;

    public Optional<User> findByDataBaseColumn(String column,String value) {
        return Optional.ofNullable(userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq(column, value)
                        .eq(UserColumn.STATUS, UserConstant.Status.NORMAL)
        ));
    }

    public Optional<User> findByEmail(String email) {
        return Optional.ofNullable(userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq(UserColumn.EMAIL, email)
        ));
    }

    public Optional<User> findByUid(Long uid) {
        return Optional.ofNullable(userMapper.selectOne(
                new QueryWrapper<User>()
                        .eq(UserColumn.UID, uid)
                        .eq(UserColumn.STATUS, UserConstant.Status.NORMAL)
        ));
    }

    public void add(UserRegisterRequest userRegisterRequest) {
        userMapper.insert(new User(userRegisterRequest));
    }

    public boolean updatePassword(String password, Long uid) {
        return userMapper.update(
                new UpdateWrapper<User>()
                .eq(UserColumn.UID, uid)
                .set(UserColumn.PASSWORD, password)
                .set(UserColumn.UPDATE_TIME, LocalDateTime.now())) != 0;
    }

    public void update(UserUpdateRequest updateRequest, Long uid) {
        userMapper.update(
                new UpdateWrapper<User>()
                .eq(UserColumn.UID, uid)
                .set(UserColumn.UPDATE_TIME, LocalDateTime.now())
                .set(updateRequest.getUserName()!=null,UserColumn.USER_NAME, updateRequest.getUserName())
                .set(updateRequest.getAge()!=null,UserColumn.AGE, updateRequest.getAge())
                .set(updateRequest.getGender()!=null,UserColumn.GENDER, updateRequest.getGender())
                .set(updateRequest.getBirthday()!=null,UserColumn.BIRTHDAY, updateRequest.getBirthday())
        );
    }
}
