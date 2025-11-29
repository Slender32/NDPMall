package com.slender.config.configuration;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.DirectExchange;
import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfiguration {

    @Bean
    public DirectExchange emailExchange() {
        return new DirectExchange("email");
    }

    @Bean
    public Queue captchaQueue() {
        return new Queue("captcha");
    }

    @Bean
    public Binding captchaBinding() {
        return BindingBuilder.bind(captchaQueue()).to(emailExchange()).with("captcha");
    }

}
