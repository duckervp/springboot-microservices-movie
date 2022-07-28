package com.duckervn.authservice.validation.validator;

import com.duckervn.authservice.validation.annotation.NotBlankIfNotNull;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;

public class NotBlankIfNotNullValidator implements ConstraintValidator<NotBlankIfNotNull, String> {
    @Override
    public void initialize(NotBlankIfNotNull constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (Objects.isNull(value)) return true;
        if (value.trim().length() > 0) return true;
        return false;
    }
}