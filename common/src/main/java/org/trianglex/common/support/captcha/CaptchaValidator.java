package org.trianglex.common.support.captcha;

public abstract class CaptchaValidator {

    private CaptchaValidator() {

    }

    public static boolean isCaptchaTimeout(Captcha captcha) {
        return captcha.getInterval() <= System.currentTimeMillis();
    }
}
