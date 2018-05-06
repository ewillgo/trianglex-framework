package org.trianglex.common.security.auth;

public interface ApiRequest {
    String getSign();
    String getAppKey();
    String getOriginalString();
}
