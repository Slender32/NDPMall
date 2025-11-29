package com.slender.service.implement;

import com.slender.constant.RedisKey;
import com.slender.constant.RedisTime;
import com.slender.enumeration.authentication.CaptchaType;
import com.slender.service.interfase.EmailService;
import com.slender.utils.StringToolkit;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import java.time.Duration;
import java.util.Map;
import java.util.concurrent.ThreadLocalRandom;

@Slf4j
@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {
    private final JavaMailSender mailSender;
    private final TemplateEngine templateEngine;
    private final StringRedisTemplate redisTemplate;

    @Value("${spring.mail.username}")
    private String fromEmail;

    @Override
    public void sendCaptcha(CaptchaType type, String toEmail) {
        final int code = ThreadLocalRandom.current().nextInt(100000,1000000);
        String message=type.getMessage();
        Context context = new Context();
        context.setVariables(Map.of(
                "code",code,
                "messageTitle","NDP"+message+"验证码",
                "message","您正在进行"+message+"操作，请使用以下验证码："));
        String htmlContent = templateEngine.process("EmailVerificationCode", context);
        MimeMessage email = mailSender.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(email, true, "UTF-8");
            helper.setFrom("Hikari<"+fromEmail+">");
            helper.setTo(toEmail);
            helper.setSubject("［NDP AutoClosure］"+message+"验证码");
            helper.setText(htmlContent,true);
        } catch (MessagingException e) {
            log.info("邮件发送失败{}",toEmail);
        }
        mailSender.send(email);
        redisTemplate.opsForValue().set(RedisKey.Authentication.CAPTCHA_REQUEST_CACHE + toEmail,
                StringToolkit.getBlankString(), Duration.ofMillis(RedisTime.Authentication.CAPTCHA_REQUEST_TIME));
        redisTemplate.opsForValue().set(type.getRedisKey() + toEmail,
                    String.valueOf(code), Duration.ofMillis(RedisTime.Authentication.CAPTCHA_EXPIRE_TIME));
    }
}
