package com.slender.service.listener

import com.slender.config.manager.JsonParserManager
import com.slender.model.message.EmailMessage
import com.slender.service.interfase.EmailService
import org.slf4j.LoggerFactory
import org.springframework.amqp.rabbit.annotation.RabbitListener
import org.springframework.stereotype.Component

@Component
class EmailListener(
    private val emailService: EmailService,
    private val jsonParser: JsonParserManager
) {
    private val log = LoggerFactory.getLogger(EmailListener::class.java)

    @RabbitListener(queues = ["captcha"])
    fun sendEmail(message: String?) {
        try {
            val emailMessage = jsonParser.parse(message, EmailMessage::class.java)
            emailService.sendCaptcha(emailMessage.type, emailMessage.toEmail)
        } catch (e: Exception) {
            log.error("消息处理失败: {}", message, e)
        }
    }
}
