package com.slender.config.configuration

import jakarta.validation.Validation
import jakarta.validation.Validator
import jakarta.validation.ValidatorFactory
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
open class ValidationConfiguration {
    @Bean
    open fun validatorFactory(): ValidatorFactory {
        return Validation.buildDefaultValidatorFactory()
    }

    @Bean
    open fun validator(validatorFactory: ValidatorFactory): Validator {
        return validatorFactory.validator
    }
}
