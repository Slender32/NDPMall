package com.slender.service.interfase;

import com.baomidou.mybatisplus.extension.service.IService;
import com.slender.dto.CaptchaRequest;
import com.slender.dto.RegisterRequest;
import com.slender.dto.ResetRequest;
import com.slender.dto.UserUpdateRequest;
import com.slender.entity.User;
import com.slender.result.Response;
import com.slender.vo.RefreshData;

import java.util.Optional;

public interface UserService extends IService<User> {
    Optional<User> getByDataBaseColumn(String column, String value);
    Optional<User> getByEmail(String email);

    void sendCaptcha(CaptchaRequest captchaRequest);

    void register(RegisterRequest registerRequest);

    boolean reset(ResetRequest resetRequest, User user);

    void update(UserUpdateRequest updateRequest, Long uid);

    RefreshData refresh(Long uid);
}
