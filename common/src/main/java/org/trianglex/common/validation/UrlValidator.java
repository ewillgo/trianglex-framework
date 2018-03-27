package org.trianglex.common.validation;

import org.trianglex.common.util.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlValidator implements ConstraintValidator<IsURL, String> {

    @Override
    public void initialize(IsURL url) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && RegexUtils.isMatch(value, RegexUtils.URL);
    }
}
