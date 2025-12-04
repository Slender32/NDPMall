package com.slender.config.manager;

import com.slender.exception.category.ValidationException;
import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public final class ValidatorManager {
    private final Validator validator;
    public <T> void validate(T validationObject){
        Set<ConstraintViolation<T>> validateMessages = validator.validate(validationObject);
        if (!validateMessages.isEmpty()) {
            String message = validateMessages.stream()
                    .map(ConstraintViolation::getMessage)
                    .toList()
                    .getFirst();
            throw new ValidationException(message);
        }
    }
}
