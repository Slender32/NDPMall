package com.slender.service.interfase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.authentication.CaptchaRequest;
import com.slender.dto.authentication.LogoffRequest;
import com.slender.dto.user.UserRegisterRequest;
import com.slender.dto.user.UserResetRequest;
import com.slender.dto.user.UserUpdateRequest;
import com.slender.entity.User;
import com.slender.vo.RefreshData;

import java.util.Optional;

public interface UserService extends IService<User> {
    Optional<User> getByDataBaseColumn(String column, String value);
    Optional<User> getByEmail(String email);

    void sendCaptcha(CaptchaRequest captchaRequest);

    void register(UserRegisterRequest userRegisterRequest);

    void reset(UserResetRequest userResetRequest, Long uid);

    void update(UserUpdateRequest updateRequest, Long uid);

    RefreshData refresh(Long uid);

    void block(Long userUid);

    void logoff(Long uid, LogoffRequest request);
}
