package org.trianglex.common.util;

import java.util.regex.Pattern;

public abstract class RegexUtils {

    public static final Pattern UUID = Pattern.compile("^[0-9a-fA-F]{8}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{4}-[0-9a-fA-F]{12}$");
    public static final Pattern ID_CARD_18 = Pattern.compile("^(\\d{6})(\\d{4})(\\d{2})(\\d{2})(\\d{3})([0-9]|X)$");
    public static final Pattern URL = Pattern.compile("[a-zA-z]+://[^\\s]*");
    public static final Pattern PHONE = Pattern.compile("^((13[0-9])|(14[5|7])|(15([0-3]|[5-9]))|(17[3,7,8])|(18[0,5-9]))\\d{8}$");
    public static final Pattern EMAIL = Pattern.compile("[\\w!#$%&'*+/=?^_`{|}~-]+(?:\\.[\\w!#$%&'*+/=?^_`{|}~-]+)*@(?:[\\w](?:[\\w-]*[\\w])?\\.)+[\\w](?:[\\w-]*[\\w])?");
    public static final Pattern NICKNAME = Pattern.compile("[\u4e00-\u9fa50-9a-zA-Z]+");
    public static final Pattern EMOJI = Pattern.compile("[\ud83c\udc00-\ud83c\udfff]|[\ud83d\udc00-\ud83d\udfff]|[\u2600-\u27ff]", Pattern.UNICODE_CASE | Pattern.CASE_INSENSITIVE);

    private RegexUtils() {
    }

    public static boolean isMatch(String str, Pattern pattern) {
        return pattern.matcher(str).matches();
    }
}
