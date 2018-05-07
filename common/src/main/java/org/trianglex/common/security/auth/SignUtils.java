package org.trianglex.common.security.auth;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trianglex.common.util.JsonUtils;

public abstract class SignUtils {

    private static final String CHARSET = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(SignUtils.class);

    private SignUtils() {

    }

    public static <T> String generateOriginalString(T t) {
        try {
            return Base64.encodeBase64URLSafeString(JsonUtils.toJsonString(t).getBytes(CHARSET));
        } catch (Exception e) {
            logger.error(e.getMessage(), e);
            return null;
        }
    }

    public static <T> String sign(T original, String appSecret) {
        return DigestUtils.sha512Hex(
                String.format("%s::%s", appSecret, DigestUtils.md5Hex(JsonUtils.toJsonString(original))));
    }
}
