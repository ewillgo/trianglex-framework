package org.trianglex.common.validation;

import org.springframework.util.StringUtils;
import org.trianglex.common.util.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class IdCardValidator implements ConstraintValidator<IsIdCard, String> {

    @Override
    public void initialize(IsIdCard idCard) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isEmpty(value) || RegexUtils.isMatch(value, RegexUtils.ID_CARD_18);
    }
}
