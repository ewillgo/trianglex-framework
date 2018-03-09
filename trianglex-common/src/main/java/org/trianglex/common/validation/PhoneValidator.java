package org.trianglex.common.validation;

import org.trianglex.common.util.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class PhoneValidator implements ConstraintValidator<IsPhone, String> {

    @Override
    public void initialize(IsPhone phone) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && RegexUtils.isMatch(value, RegexUtils.PHONE);
    }
}
