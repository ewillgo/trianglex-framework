package org.trianglex.common.security.auth;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.trianglex.common.util.JsonUtils;

public abstract class SignUtils {

    private static final String CHARSET = "UTF-8";
    private static final Logger logger = LoggerFactory.getLogger(SignUtils.class);

    private SignUtils() {

    }

    public static <T> String sign(T original, String appSecret) {
        String dataDigest = (original instanceof String)
                ? (String) original : DigestUtils.md5Hex(JsonUtils.toJsonString(original));
        return String.format("%s%s", dataDigest, DigestUtils.sha512Hex(String.format("%s_%s", appSecret, dataDigest)));
    }
}
