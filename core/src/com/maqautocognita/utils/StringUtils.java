package com.maqautocognita.utils;

/**
 * @author sc.chi csc19840914@gmail.com
 */
public class StringUtils {

    public static String removeAllSpace(String value) {
        if (StringUtils.isNotBlank(value)) {
            return value.replaceAll("\\s", "");
        }

        return null;
    }

    public static boolean isNotBlank(String value) {
        return !isBlank(value);
    }

    public static boolean isBlank(String value) {
        return null == value || value.trim().equals("");

    }

    public static String trim(String value) {
        if (StringUtils.isNotBlank(value)) {
            return value.trim();
        }

        return null;
    }

    public static boolean isEquals(String expectedValue, String compareValue) {
        if (isBlank(expectedValue) && isBlank(compareValue)) {
            return true;
        }

        if (isNotBlank(expectedValue)) {
            return expectedValue.equalsIgnoreCase(compareValue);
        }

        return false;

    }
}
