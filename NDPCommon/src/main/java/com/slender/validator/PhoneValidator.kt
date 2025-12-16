package com.slender.validator

import jakarta.validation.ConstraintValidator
import com.slender.annotation.Phone
import jakarta.validation.ConstraintValidatorContext
import java.util.regex.Pattern

class PhoneValidator : ConstraintValidator<Phone, String> {
    override fun isValid(phone: String?, context: ConstraintValidatorContext?): Boolean {
        if (phone == null) return true
        return phone.matches(Regex("^1[3-9]\\d{9}$"))
    }
}
