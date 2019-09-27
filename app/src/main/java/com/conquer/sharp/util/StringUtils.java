package com.conquer.sharp.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    public static int getChineseCount(String str) {
        String regEx = "[\\u4e00-\\u9fa5]"; // unicode编码，判断是否为汉字
        int count = 0;
        Pattern p = Pattern.compile(regEx);
        Matcher m = p.matcher(str);
        while (m.find()) {
            for (int i = 0; i <= m.groupCount(); i++) {
                count = count + 1;
            }
        }
        return count;
    }
}
