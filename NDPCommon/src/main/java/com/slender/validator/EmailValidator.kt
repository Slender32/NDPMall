package com.slender.validator

import com.slender.annotation.Email
import jakarta.validation.ConstraintValidator
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class EmailValidator : ConstraintValidator<Email, String> {
    override fun isValid(
        email: String?,
        constraintValidatorContext: ConstraintValidatorContext?
    ): Boolean {
        if (email == null) return true
        return email.matches(Regex("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$"))
    }
}
