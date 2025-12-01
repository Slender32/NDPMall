package com.slender.service.implement;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.slender.config.manager.JsonParserManager;
import com.slender.constant.other.RabbitMQConstant;
import com.slender.constant.other.RedisKey;
import com.slender.constant.other.RedisTime;
import com.slender.dto.authentication.CaptchaRequest;
import com.slender.dto.user.UserRegisterRequest;
import com.slender.dto.user.UserResetRequest;
import com.slender.dto.user.UserUpdateRequest;
import com.slender.entity.User;
import com.slender.exception.authentication.login.UserNotFoundException;
import com.slender.exception.authentication.captcha.CaptchaErrorException;
import com.slender.exception.authentication.captcha.CaptchaPersistenceException;
import com.slender.exception.request.FrequentRequestCaptchaException;
import com.slender.mapper.UserMapper;
import com.slender.model.message.EmailMessage;
import com.slender.model.cache.LoginDataCache;
import com.slender.repository.UserRepository;
import com.slender.service.interfase.UserService;
import com.slender.utils.JwtToolkit;
import com.slender.vo.RefreshData;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.Duration;
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
    public void register(UserRegisterRequest userRegisterRequest) {
        String captcha = redisTemplate.opsForValue().get(RedisKey.Authentication.CAPTCHA_REGISTER_CACHE + userRegisterRequest.getEmail());
        if (captcha == null) throw new CaptchaPersistenceException();
        if (!userRegisterRequest.getCaptcha().toString().equals(captcha)) throw new CaptchaErrorException();
        redisTemplate.delete(RedisKey.Authentication.CAPTCHA_REGISTER_CACHE + userRegisterRequest.getEmail());
        userRegisterRequest.setPassword(passwordEncoder.encode(userRegisterRequest.getPassword()));
        userRepository.add(userRegisterRequest);
    }

    @Override
    public boolean reset(UserResetRequest userResetRequest, User user) {
        String captcha = redisTemplate.opsForValue().get(RedisKey.Authentication.CAPTCHA_RESET_CACHE + user.getUid());
        if (captcha == null) throw new CaptchaPersistenceException();
        if (!userResetRequest.getCaptcha().toString().equals(captcha)) throw new CaptchaErrorException();
        redisTemplate.delete(RedisKey.Authentication.CAPTCHA_RESET_CACHE + user.getUid());
        return userRepository.updatePassword(passwordEncoder.encode(userResetRequest.getPassword()), user.getUid());
    }

    @Override
    public void update(UserUpdateRequest updateRequest, Long uid) {
        userRepository.update(updateRequest, uid);
    }

    @Override
    public RefreshData refresh(Long uid) {
        return userRepository.findByUid(uid).map(user -> {
            final String data = jsonParser.format(new LoginDataCache(user.getUid(), user.getUserName(), user.getAuthority()));
            redisTemplate.opsForValue().set(RedisKey.Authentication.USER_LOGIN_CACHE+uid,data, RedisTime.Authentication.ACCESS_TOKEN_EXPIRE_TIME);
            return new RefreshData(uid, user.getUserName(), JwtToolkit.getAccessToken(uid));
        }).orElseThrow(UserNotFoundException::new);
    }
}
