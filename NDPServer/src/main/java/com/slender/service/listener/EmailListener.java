package com.slender.service.listener;

import com.slender.config.manager.JsonParserManager;
import com.slender.model.message.EmailMessage;
import com.slender.service.interfase.EmailService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

@Slf4j
@Component
@RequiredArgsConstructor
public class EmailListener {
    private final EmailService emailService;
    private final JsonParserManager jsonParser;

    @RabbitListener(queues = "captcha")
    public void sendEmail(String message){
        try {
            EmailMessage emailMessage = jsonParser.parse(message, EmailMessage.class);
            emailService.sendCaptcha(emailMessage.type(), emailMessage.toEmail());
        } catch (Exception e) {
            log.error("消息处理失败: {}", message, e);
        }
    }
}
