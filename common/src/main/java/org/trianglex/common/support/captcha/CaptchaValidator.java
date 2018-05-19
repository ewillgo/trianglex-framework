package org.trianglex.common.support.captcha;

import org.springframework.util.StringUtils;

public abstract class CaptchaValidator {

    private CaptchaValidator() {

    }

    public static boolean isCaptchaTimeout(Captcha captcha) {
        return captcha.getInterval() <= System.currentTimeMillis();
    }

    public static boolean equalsIgnoreCase(Captcha clientCaptcha, Captcha serverCaptcha) {
        if (clientCaptcha == null || serverCaptcha == null
                || StringUtils.isEmpty(clientCaptcha.getCaptcha()) || StringUtils.isEmpty(serverCaptcha.getCaptcha())) {
            return false;
        }

        return clientCaptcha.getCaptcha().equalsIgnoreCase(serverCaptcha.getCaptcha());
    }
}
