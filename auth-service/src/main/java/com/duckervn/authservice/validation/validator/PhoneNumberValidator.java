package com.duckervn.authservice.validation.validator;

import com.duckervn.authservice.validation.annotation.ValidPhoneNumber;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PhoneNumberValidator implements ConstraintValidator<ValidPhoneNumber, String> {

    @Override
    public void initialize(ValidPhoneNumber constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String phone, ConstraintValidatorContext context) {
        return validatePhoneNumber(phone);
    }

    private boolean validatePhoneNumber(String phone) {
        // if the phone field is not provide then cancel check
        if (Objects.isNull(phone)) return true;
        String PHONE_REGEX = "^\\d{10,11}$";
        Pattern pattern = Pattern.compile(PHONE_REGEX);
        Matcher matcher = pattern.matcher(phone);
        if (matcher.matches()) return true;
        return false;
    }
}