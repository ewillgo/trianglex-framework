package org.trianglex.common.validation;

import org.springframework.util.StringUtils;
import org.trianglex.common.util.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UrlValidator implements ConstraintValidator<IsURL, String> {

    @Override
    public void initialize(IsURL url) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isEmpty(value) || RegexUtils.isMatch(value, RegexUtils.URL);
    }
}
