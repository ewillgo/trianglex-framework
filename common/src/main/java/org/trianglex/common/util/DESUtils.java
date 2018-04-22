package org.trianglex.common.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.Base64Utils;

import javax.crypto.Cipher;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;

public abstract class DESUtils {

    private static final String CHARSET = "UTF-8";
    private static final String DESEDE = "desede";
    private static final String ECB_CIPHER_INSTANCE_NAME = DESEDE + "/ECB/PKCS5Padding";
    private static final String CBC_CIPHER_INSTANCE_NAME = DESEDE + "/CBC/PKCS5Padding";
    private static final Logger logger = LoggerFactory.getLogger(DESUtils.class);

    private DESUtils() {

    }

    public static String encodeToUrlSafeString(byte[] bytes) {
        return Base64Utils.encodeToUrlSafeString(bytes);
    }

    public static byte[] decodeFromUrlSafeString(String content) {
        return Base64Utils.decodeFromUrlSafeString(content);
    }

    public static byte[] des3EncodeECB(byte[] data, String key) {

        try {

            Cipher cipher = Cipher.getInstance(ECB_CIPHER_INSTANCE_NAME);
            cipher.init(Cipher.ENCRYPT_MODE,
                    SecretKeyFactory.getInstance(DESEDE).generateSecret(new DESedeKeySpec(key.getBytes(CHARSET))));

            return cipher.doFinal(data);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public static byte[] des3DecodeECB(byte[] data, String key) {

        try {

            Cipher cipher = Cipher.getInstance(ECB_CIPHER_INSTANCE_NAME);
            cipher.init(Cipher.DECRYPT_MODE,
                    SecretKeyFactory.getInstance(DESEDE).generateSecret(new DESedeKeySpec(key.getBytes(CHARSET))));

            return cipher.doFinal(data);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public static byte[] des3EncodeCBC(byte[] data, byte[] key, byte[] keyiv) {

        try {

            Cipher cipher = Cipher.getInstance(CBC_CIPHER_INSTANCE_NAME);
            cipher.init(Cipher.ENCRYPT_MODE, SecretKeyFactory.getInstance(DESEDE)
                    .generateSecret(new DESedeKeySpec(key)), new IvParameterSpec(keyiv));

            return cipher.doFinal(data);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }

    public static byte[] des3DecodeCBC(byte[] data, byte[] key, byte[] keyiv) {

        try {

            Cipher cipher = Cipher.getInstance(CBC_CIPHER_INSTANCE_NAME);
            cipher.init(Cipher.DECRYPT_MODE, SecretKeyFactory.getInstance(DESEDE)
                    .generateSecret(new DESedeKeySpec(key)), new IvParameterSpec(keyiv));

            return cipher.doFinal(data);

        } catch (Exception e) {
            logger.error(e.getMessage(), e);
        }

        return null;
    }
}
