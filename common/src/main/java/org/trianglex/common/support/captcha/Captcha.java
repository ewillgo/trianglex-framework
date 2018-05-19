package org.trianglex.common.support.captcha;

import java.io.Serializable;

public class Captcha implements Serializable {

    private static final long serialVersionUID = -6786067693593717930L;
    private String captcha;
    private long interval;

    public Captcha(String captcha) {
        this(captcha, -1);
    }

    public Captcha(String captcha, long interval) {
        this.captcha = captcha;
        this.interval = interval;
    }

    public String getCaptcha() {
        return captcha;
    }

    public void setCaptcha(String captcha) {
        this.captcha = captcha;
    }

    public long getInterval() {
        return interval;
    }

    public void setInterval(long interval) {
        this.interval = interval;
    }
}
