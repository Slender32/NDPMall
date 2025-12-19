package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.config.manager.FileManager;
import com.slender.config.manager.JsonParserManager;
import com.slender.config.manager.UserValidatorManager;
import com.slender.constant.other.RabbitMQConstant;
import com.slender.constant.other.RedisKey;
import com.slender.constant.other.RedisTime;
import com.slender.dto.authentication.CaptchaRequest;
import com.slender.dto.authentication.LogoffRequest;
import com.slender.dto.user.UserRegisterRequest;
import com.slender.dto.user.UserResetRequest;
import com.slender.dto.user.UserUpdateRequest;
import com.slender.entity.User;
import com.slender.enumeration.FileType;
import com.slender.enumeration.authentication.CaptchaType;
import com.slender.enumeration.authentication.ResetType;
import com.slender.exception.authentication.login.LoginStatusException;
import com.slender.exception.user.UserNotFoundException;
import com.slender.exception.authentication.captcha.CaptchaPersistenceException;
import com.slender.exception.request.FrequentRequestCaptchaException;
import com.slender.mapper.UserMapper;
import com.slender.model.message.EmailMessage;
import com.slender.model.cache.LoginDataCache;
import com.slender.repository.UserRepository;
import com.slender.service.interfase.UserService;
import com.slender.utils.JwtToolkit;
import com.slender.utils.StringToolkit;
import com.slender.vo.FileData;
import com.slender.vo.RefreshData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    private final UserRepository userRepository;
    private final StringRedisTemplate redisTemplate;
    private final RabbitTemplate rabbitTemplate;
    private final JsonParserManager jsonParser;
    private final PasswordEncoder passwordEncoder;
    private final FileManager fileManager;
    private final UserValidatorManager userValidatorManager;

    @Override
    public Optional<User> getByDataBaseColumn(String column, String value) {
        return userRepository.findByDataBaseColumn(column, value);
    }

    @Override
    public Optional<User> getByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public void sendCaptcha(CaptchaRequest captchaRequest) {
        final long expire = redisTemplate.getExpire(RedisKey.Authentication.CAPTCHA_REQUEST_CACHE + captchaRequest.getEmail());
        if (expire == -2) {
            final String email = captchaRequest.getEmail();
            String message = jsonParser.format(new EmailMessage(captchaRequest.getType(), email));
            rabbitTemplate.convertAndSend(RabbitMQConstant.EMAIL_EXCHANGE, RabbitMQConstant.EMAIL_CAPTCHA_ROUTING_KEY, message);
        } else if (expire == -1) throw new CaptchaPersistenceException();
        else throw new FrequentRequestCaptchaException("还有" + expire + "s才能继续获取验证码");
    }

    @Override
    public void register(UserRegisterRequest registerRequest) {
        userValidatorManager.validateCaptcha(registerRequest.getEmail(), registerRequest.getCaptcha());
        registerRequest.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        this.save(new User(registerRequest));
        userValidatorManager.removeCaptcha(registerRequest.getCaptcha());
    }

    @Override
    public void reset(UserResetRequest resetRequest, Long uid) {
        userValidatorManager.validateCaptcha(uid, resetRequest.getCaptcha(), CaptchaType.RESET);
        if(resetRequest.getType() == ResetType.PASSWORD) resetRequest.setPassword(passwordEncoder.encode(resetRequest.getPassword()));
        userRepository.update(resetRequest, uid);
        userValidatorManager.removeCaptcha(resetRequest.getCaptcha());
    }

    @Override
    public void update(UserUpdateRequest updateRequest, Long uid) {
        userRepository.update(updateRequest, uid);
    }

    @Override
    public RefreshData refresh(Long uid) {
        return userRepository.findByUid(uid).map(user -> {
            final String data = jsonParser.format(new LoginDataCache(user.getUid(), user.getUserName(), user.getEmail(), user.getAuthority()));
            redisTemplate.opsForValue().set(RedisKey.Authentication.USER_LOGIN_CACHE+uid,data, RedisTime.Authentication.ACCESS_TOKEN_EXPIRE_TIME);
            return new RefreshData(uid, user.getUserName(), JwtToolkit.getAccessToken(uid));
        }).orElseThrow(UserNotFoundException::new);
    }

    @Override
    public void block(Long userUid) {
        boolean delete = redisTemplate.delete(RedisKey.Authentication.USER_LOGIN_CACHE + userUid);
        if(!delete) throw new LoginStatusException();
        redisTemplate.opsForValue().set(RedisKey.Authentication.USER_BLOCK_CACHE+userUid,
                StringToolkit.getBlankString(), RedisTime.Authentication.REFRESH_TOKEN_EXPIRE_TIME);
    }

    @Override
    public void logoff(Long uid, LogoffRequest request) {
        userValidatorManager.validateCaptcha(uid, request.getCaptcha(), CaptchaType.LOGOFF);
        this.removeById(uid);
        userValidatorManager.removeCaptcha(request.getCaptcha());
    }

    @Override
    public FileData uploadAvatar(MultipartFile file) {
        try {
            String url = fileManager.upload(file.getOriginalFilename(), file.getBytes(), FileType.USER);
            return new FileData(url);
        } catch (IOException e) {
            log.error("获取文件内容失败",e);
        }
        return null;
    }
}
