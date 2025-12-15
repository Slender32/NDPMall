package com.slender.config.manager

import com.slender.exception.category.ValidationException
import jakarta.validation.ConstraintViolation
import jakarta.validation.Validator
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Component

@Component
@RequiredArgsConstructor
class ValidatorManager(
    private val validator: Validator
) {
    fun <T> validate(validationObject: T) {
        val validateMessages = validator.validate(validationObject)
        if (!validateMessages.isEmpty()) {
            val message = validateMessages.stream()
                .map { obj: ConstraintViolation<T> -> obj.message }
                .toList()
                .first()
            throw ValidationException(message)
        }
    }
}
