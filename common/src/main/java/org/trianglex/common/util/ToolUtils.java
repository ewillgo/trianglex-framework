package org.trianglex.common.util;

import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.util.UUID;

public abstract class ToolUtils {

    private static final String CHARSET = "UTF-8";

    private ToolUtils() {
    }

    public static String encodeUrl(String url) {
        String encodeUrl = null;
        try {
            encodeUrl = URLEncoder.encode(url, CHARSET);
        } catch (UnsupportedEncodingException ignore) {
        }
        return encodeUrl;
    }

    public static String decodeUrl(String url) {
        String decodeUrl = null;
        try {
            decodeUrl = URLDecoder.decode(url, CHARSET);
        } catch (UnsupportedEncodingException ignore) {
        }
        return decodeUrl;
    }

    public static String getRemoteIp(HttpServletRequest request) {
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }

    public static String getUUID() {
        return UUID.randomUUID().toString().replaceAll("-", "");
    }

    public static int extractGender(String idCard) {

        if (StringUtils.isEmpty(idCard)) {
            return 0;
        }

        idCard = idCard.replaceAll("\\s+", "");
        int genderCode = Integer.parseInt(idCard.substring(idCard.length() - 2, idCard.length() - 1));

        return idCard.length() == 18 ? (genderCode % 2 == 0 ? 2 : 1) : 0;
    }

    public static String extractBirth(String idCard) {

        if (StringUtils.isEmpty(idCard)) {
            return null;
        }

        String birthday = null;
        idCard = idCard.replaceAll("\\s+", "");
        int idCardLength = idCard.length();

        if (idCardLength == 18) {
            birthday = idCard.substring(6, 10) + "-" + idCard.substring(10, 12) + "-" + idCard.substring(12, 14);
        }

        return birthday;
    }

    private static final String XML_HTTP_REQUEST = "XMLHttpRequest";
    private static final String X_REQUESTED_WITH = "x-requested-with";

    public static boolean isAjax(HttpServletRequest request) {
        return request.getHeader(X_REQUESTED_WITH) != null
                && XML_HTTP_REQUEST.equalsIgnoreCase(request.getHeader(X_REQUESTED_WITH));
    }
}
