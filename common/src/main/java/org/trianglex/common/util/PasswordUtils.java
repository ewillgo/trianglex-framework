package org.trianglex.common.util;

import org.apache.commons.codec.digest.Crypt;
import org.springframework.util.Base64Utils;

import java.security.SecureRandom;
import java.util.Random;

public abstract class PasswordUtils {

    private static final int SALT_BYTES = 16;
    private static final String SHA256_CRYPT_PREFIX = "$5$";
    private static final String SHA512_CRYPT_PREFIX = "$6$";
    private static final Random SECURE_RANDOM = new SecureRandom();

    private PasswordUtils() {

    }

    public static String password(String password, String salt) {
        return Crypt.crypt(password, salt);
    }

    public static String salt256() {
        byte[] salt = new byte[SALT_BYTES];
        SECURE_RANDOM.nextBytes(salt);
        return SHA256_CRYPT_PREFIX + Base64Utils.encodeToString(salt);
    }

    public static String salt512() {
        byte[] salt = new byte[SALT_BYTES];
        SECURE_RANDOM.nextBytes(salt);
        return SHA512_CRYPT_PREFIX + Base64Utils.encodeToString(salt);
    }
}
