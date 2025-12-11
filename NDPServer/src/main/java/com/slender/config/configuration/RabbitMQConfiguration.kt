package com.slender.config.configuration

import org.springframework.amqp.core.Binding
import org.springframework.amqp.core.BindingBuilder
import org.springframework.amqp.core.DirectExchange
import org.springframework.amqp.core.Queue
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class RabbitMQConfiguration {
    @Bean
    open fun emailExchange(): DirectExchange {
        return DirectExchange("email")
    }

    @Bean
    open fun orderExchange(): DirectExchange {
        return DirectExchange("order")
    }

    @Bean
    open fun captchaQueue(): Queue {
        return Queue("captcha")
    }

    @Bean
    open fun orderQueue(): Queue {
        return Queue("order")
    }

    @Bean
    open fun captchaBinding(): Binding {
        return BindingBuilder.bind(captchaQueue()).to(emailExchange()).with("captcha")
    }

    @Bean
    open fun orderBinding(): Binding {
        return BindingBuilder.bind(orderQueue()).to(orderExchange()).with("order")
    }
}
