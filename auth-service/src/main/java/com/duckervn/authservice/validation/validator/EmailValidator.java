package com.duckervn.authservice.validation.validator;

import com.duckervn.authservice.validation.annotation.ValidEmail;
import org.apache.commons.lang.StringUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public void initialize(ValidEmail constraintAnnotation) {
        ConstraintValidator.super.initialize(constraintAnnotation);
    }

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        return validateEmail(email);
    }

    private boolean validateEmail(String email) {
        if (StringUtils.isBlank(email)) {
            return false;
        }
        String EMAIL_REGEX = "^(\\w+)(\\.\\w+)*@(\\w+)(\\.\\w+)*(\\.[A-Za-z]{2,4})$";
        Pattern pattern = Pattern.compile(EMAIL_REGEX);
        Matcher matcher = pattern.matcher(email);
        if (matcher.matches()) return true;
        return false;
    }
}
