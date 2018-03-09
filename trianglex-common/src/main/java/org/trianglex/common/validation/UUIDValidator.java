package org.trianglex.common.validation;

import org.trianglex.common.util.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class UUIDValidator implements ConstraintValidator<IsUUID, String> {

    @Override
    public void initialize(IsUUID isUUID) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return value != null && RegexUtils.isMatch(value, RegexUtils.UUID);
    }
}
