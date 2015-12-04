package com.smilehacker.coffeeknife.common.string;

import android.util.Base64;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.LinkedList;

/**
 * Created by kleist on 14/10/20.
 */
public class StringUtils {
    public static String BASE64ToUtf8(String text) {
        byte[] data = Base64.decode(text, Base64.DEFAULT);
        try {
            return new String(data, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }

    public static String ISO88591ToUtf8(String text) {
        try {
            return new String(text.getBytes("8859_1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            return text;
        }
    }

    public static String unicodeToUtf8(String theString) {
        if (theString == null || theString.length() == 0) {
            return "";
        }
        char aChar;
        int len = theString.length();
        StringBuffer outBuffer = new StringBuffer(len);
        for (int x = 0; x < len;) {
            aChar = theString.charAt(x++);
            if (aChar == '\\') {
                aChar = theString.charAt(x++);
                if (aChar == 'u') {
                    // Read the xxxx
                    int value = 0;
                    for (int i = 0; i < 4; i++) {
                        aChar = theString.charAt(x++);
                        switch (aChar) {
                            case '0':
                            case '1':
                            case '2':
                            case '3':
                            case '4':
                            case '5':
                            case '6':
                            case '7':
                            case '8':
                            case '9':
                                value = (value << 4) + aChar - '0';
                                break;
                            case 'a':
                            case 'b':
                            case 'c':
                            case 'd':
                            case 'e':
                            case 'f':
                                value = (value << 4) + 10 + aChar - 'a';
                                break;
                            case 'A':
                            case 'B':
                            case 'C':
                            case 'D':
                            case 'E':
                            case 'F':
                                value = (value << 4) + 10 + aChar - 'A';
                                break;
                            default:
                                throw new IllegalArgumentException(
                                        "Malformed   \\uxxxx   encoding.");
                        }
                    }
                    outBuffer.append((char) value);
                } else {
                    if (aChar == 't')
                        aChar = '\t';
                    else if (aChar == 'r')
                        aChar = '\r';
                    else if (aChar == 'n')
                        aChar = '\n';
                    else if (aChar == 'f')
                        aChar = '\f';
                    outBuffer.append(aChar);
                }
            } else
                outBuffer.append(aChar);
        }
        return outBuffer.toString();
    }

    public static String join(ArrayList<String> list, String c) {
        if (list == null || list.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        for (int i = 0, len = list.size(); i < len; i++) {
            if (i != 0) {
                builder.append(c);
            }

            builder.append(list.get(i));
        }

        return builder.toString();
    }

    public static String join(LinkedList<String> list, String c) {
        if (list == null || list.size() == 0) {
            return "";
        }

        StringBuilder builder = new StringBuilder();
        boolean isFirst = true;
        for (String str : list) {
            if (!isFirst) {
                builder.append(c);
            } else {
                isFirst = false;
            }
            builder.append(str);
        }

        return builder.toString();
    }
}
