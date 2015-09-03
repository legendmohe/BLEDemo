package com.example.bledemo.util;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class StringUtil {
    public static String ByteArrayToHexString(byte[] src) {
        if (src == null || src.length == 0) {
            return "";
        }
        final StringBuilder builder = new StringBuilder();
        for(byte b : src) {
            builder.append(String.format("%02x", b));
        }
        return builder.toString();
    }
    
    public static byte[] hexStringToByteArray(String s) {
        int len = s.length();
        byte[] data = new byte[len / 2];
        for (int i = 0; i < len; i += 2) {
            data[i / 2] = (byte) ((Character.digit(s.charAt(i), 16) << 4)
                                 + Character.digit(s.charAt(i+1), 16));
        }
        return data;
    }
    
    public static String join(String sep, Collection<String> src) {
        StringBuilder builder = new StringBuilder();
        for (String string : src) {
            builder.append(string);
            builder.append(sep);
        }
        if (builder.length() != 0) {
            builder.delete(builder.length() - sep.length(), builder.length() - 1);
        }
        return builder.toString();
    }
}
