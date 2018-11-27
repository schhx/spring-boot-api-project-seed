package com.company.project.core.utils;

import java.security.MessageDigest;

/**
 * @author shanchao
 * @date 2018-11-27
 */
public class StringUtil {
    public static boolean isCapitalized(String str) {
        if (str == null || str.isEmpty())
            return false;

        return Character.isUpperCase(str.charAt(0));
    }

    public static String deCap(String str) {
        if (str == null || str.isEmpty())
            return str;
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toLowerCase(str.charAt(0)));
        sb.append(str.substring(1));
        return sb.toString();
    }

    public static String capitalize (String str) {
        if (str == null || str.isEmpty())
            return str;
        StringBuilder sb = new StringBuilder();
        sb.append(Character.toUpperCase(str.charAt(0)));
        sb.append(str.substring(1));
        return sb.toString();
    }

    public static boolean empty(String str) {
        return str==null || str.isEmpty();
    }

    public static String join(String sep, Object... fields) {
        StringBuilder sb = new StringBuilder();
        for (Object field: fields) {
            if (sb.length() > 0) {
                sb.append(sep);
            }
            sb.append(field);
        }
        return sb.toString();
    }

    public static String camelCaseToUnderbar(String symbol) {
        return symbol.replaceAll("([^_A-Z])([A-Z])", "$1_$2").toLowerCase();
    }

    public static String underbarToCamelCase(String symbol) {
        String[] fields = symbol.split("_");
        StringBuilder sb = new StringBuilder();
        for(String field: fields) {
            sb.append(capitalize(field));
        }
        return deCap(sb.toString());
    }
}
