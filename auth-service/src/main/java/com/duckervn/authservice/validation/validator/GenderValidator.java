package com.duckervn.authservice.validation.validator;

import com.duckervn.authservice.domain.entity.Gender;
import com.duckervn.authservice.validation.annotation.ValidGender;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.Objects;

public class GenderValidator implements ConstraintValidator<ValidGender, String> {

    @Override
    public void initialize(ValidGender constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String gender, ConstraintValidatorContext context) {
        return validateGender(gender);
    }

    private boolean validateGender(String gender) {
        // if gender is not passed in then skip check gender
        if (Objects.isNull(gender)) return true;
        if (Arrays.stream(Gender.values()).anyMatch(gd -> gd.name().equals(gender))) return true;
        return false;
    }
}
