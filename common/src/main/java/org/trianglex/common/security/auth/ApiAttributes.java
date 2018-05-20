package org.trianglex.common.security.auth;

public class ApiAttributes implements ApiRequest {

    private String sign;
    private String appKey;

    @Override
    public String getSign() {
        return sign;
    }

    public void setSign(String sign) {
        this.sign = sign;
    }

    @Override
    public String getAppKey() {
        return appKey;
    }

    public void setAppKey(String appKey) {
        this.appKey = appKey;
    }

}
