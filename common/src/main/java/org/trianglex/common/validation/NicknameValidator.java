package org.trianglex.common.validation;

import org.springframework.util.StringUtils;
import org.trianglex.common.util.RegexUtils;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class NicknameValidator implements ConstraintValidator<IsNickname, String> {

    @Override
    public void initialize(IsNickname nickname) {
    }

    @Override
    public boolean isValid(String value, ConstraintValidatorContext constraintValidatorContext) {
        return StringUtils.isEmpty(value) || RegexUtils.isMatch(value, RegexUtils.NICKNAME);
    }
}
