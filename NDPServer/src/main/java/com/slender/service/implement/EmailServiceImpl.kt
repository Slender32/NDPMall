package com.slender.service.implement

import com.slender.constant.other.RedisKey
import com.slender.constant.other.RedisTime
import com.slender.enumeration.authentication.CaptchaType
import com.slender.exception.email.EmailSendFailedException
import com.slender.service.interfase.EmailService
import com.slender.utils.StringToolkit
import jakarta.mail.MessagingException
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.mail.javamail.MimeMessageHelper
import org.springframework.stereotype.Service
import org.thymeleaf.TemplateEngine
import org.thymeleaf.context.Context
import java.util.concurrent.ThreadLocalRandom

@Service
class EmailServiceImpl(
    private val mailSender: JavaMailSender,
    private val templateEngine: TemplateEngine,
    private val redisTemplate: StringRedisTemplate
) : EmailService {
    private val log = LoggerFactory.getLogger(javaClass)

    @Value($$"${spring.mail.username}")
    private lateinit var fromEmail: String

    override fun sendCaptcha(type: CaptchaType, toEmail: String) {
        val code = ThreadLocalRandom.current().nextInt(100000, 1000000)
        val message = type.message

        val context = Context().apply {
            setVariables(
                mapOf(
                    "code" to code,
                    "messageTitle" to "NDP${message}验证码",
                    "message" to "您正在进行${message}操作，请使用以下验证码："
                )
            )
        }

        val htmlContent = templateEngine.process("EmailVerificationCode", context)
        val email = mailSender.createMimeMessage().apply {
            try {
                MimeMessageHelper(this, true, "UTF-8").apply {
                    setFrom("Hikari<$fromEmail>")
                    setTo(toEmail)
                    subject = "［NDP AutoClosure］${message}验证码"
                    setText(htmlContent, true)
                }
            } catch (_: MessagingException) {
                log.error("邮件发送失败 {}", toEmail)
                throw EmailSendFailedException()
            }
        }

        mailSender.send(email)

        redisTemplate.opsForValue().apply {
            set(
                RedisKey.Authentication.CAPTCHA_REQUEST_CACHE + toEmail,
                StringToolkit.blankString,
                RedisTime.Authentication.CAPTCHA_REQUEST_TIME
            )
            set(
                type.redisKey + toEmail,
                code.toString(),
                RedisTime.Authentication.CAPTCHA_EXPIRE_TIME
            )
        }
    }
}