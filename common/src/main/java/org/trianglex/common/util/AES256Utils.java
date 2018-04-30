package org.trianglex.common.util;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.spec.SecretKeySpec;
import java.security.SecureRandom;
import java.security.Security;

public abstract class AES256Utils {

    private static final String CHARSET = "UTF-8";
    private static final int KEY_GENERATOR_BIT = 256;
    private static final String ALGORITHM_NAME = "AES";
    private static final String SECURE_RANDOM_ALGORITHM = "SHA1PRNG";
    private static final String CIPHER_INSTANCE_NAME = "AES/ECB/PKCS7Padding";

    private static Logger logger = LoggerFactory.getLogger(AES256Utils.class);

    static {
        if (Security.getProvider(BouncyCastleProvider.PROVIDER_NAME) == null) {
            Security.addProvider(new BouncyCastleProvider());
        }
    }

    private AES256Utils() {

    }

    public static String encodeToUrlSafeString(byte[] data) {
        return Base64Utils.encodeToUrlSafeString(data);
    }

    public static byte[] decodeFromUrlSafeString(String content) {
        return Base64Utils.decodeFromUrlSafeString(content);
    }

    public static byte[] encrypt(byte[] data, String salt) {

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_NAME);

            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM, "SUN");
            secureRandom.setSeed(salt.getBytes(CHARSET));

            keyGenerator.init(KEY_GENERATOR_BIT, secureRandom);
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME, BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(keyGenerator.generateKey().getEncoded(), ALGORITHM_NAME));
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("AES encrypt error.", e);
        }

        return null;
    }

    public static byte[] decrypt(byte[] data, String salt) {

        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM_NAME);

            SecureRandom secureRandom = SecureRandom.getInstance(SECURE_RANDOM_ALGORITHM, "SUN");
            secureRandom.setSeed(salt.getBytes(CHARSET));

            keyGenerator.init(KEY_GENERATOR_BIT, secureRandom);
            Cipher cipher = Cipher.getInstance(CIPHER_INSTANCE_NAME, BouncyCastleProvider.PROVIDER_NAME);
            cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(keyGenerator.generateKey().getEncoded(), ALGORITHM_NAME));
            return cipher.doFinal(data);
        } catch (Exception e) {
            logger.error("AES decrypt error.", e);
        }

        return null;
    }

}
