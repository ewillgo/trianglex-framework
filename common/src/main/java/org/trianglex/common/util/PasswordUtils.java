package org.trianglex.common.util;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.codec.digest.Crypt;

import java.security.SecureRandom;
import java.util.Random;
import java.util.regex.Pattern;

public abstract class PasswordUtils {

    private static final int SALT_BYTES = 16;
    private static final String SHA256_CRYPT_PREFIX = "$5$";
    private static final String SHA512_CRYPT_PREFIX = "$6$";
    private static final Random SECURE_RANDOM = new SecureRandom();
    private static final Pattern SALT_PATTERN = Pattern.compile("^\\$([56])\\$(rounds=(\\d+)\\$)?([\\.\\/a-zA-Z0-9]{1,16}).*");

    private PasswordUtils() {

    }

    public static String password(String password, String salt) {
        return Crypt.crypt(password, salt);
    }

    public static String salt256() {
        return salt(SHA256_CRYPT_PREFIX);
    }

    public static String salt512() {
        return salt(SHA512_CRYPT_PREFIX);
    }

    private static String salt(String cryptPrefix) {
        String saltString;
        byte[] salt = new byte[SALT_BYTES];
        do {
            SECURE_RANDOM.nextBytes(salt);
            saltString = cryptPrefix + Base64.encodeBase64String(salt);
        } while (!SALT_PATTERN.matcher(saltString).find());
        return saltString;
    }

}
