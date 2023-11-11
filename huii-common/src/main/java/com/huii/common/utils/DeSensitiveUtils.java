package com.huii.common.utils;

/**
 * 敏感字替换工具
 *
 * @author huii
 */
public class DeSensitiveUtils {

    private static final char ReplaceChar = '*';

    public static String hideStr(CharSequence str, int startInclude, int endExclude) {
        if (str.isEmpty()) {
            return str.toString();
        } else {
            String originalStr = str.toString();
            int[] strCodePoints = originalStr.codePoints().toArray();
            int strLength = strCodePoints.length;
            if (startInclude > strLength) {
                return originalStr;
            } else {
                if (endExclude > strLength) {
                    endExclude = strLength;
                }
                if (startInclude > endExclude) {
                    return originalStr;
                } else {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (int i = 0; i < strLength; ++i) {
                        if (i >= startInclude && i < endExclude) {
                            stringBuilder.append(ReplaceChar);
                        } else {
                            stringBuilder.append(new String(strCodePoints, i, 1));
                        }
                    }
                    return stringBuilder.toString();
                }
            }
        }
    }
}
