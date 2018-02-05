package com.longyg.frontend.Utils;

import java.util.List;

public class CommonUtil {

    public static String listToString(List<String> list) {
        if (null == list || list.size() < 1) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        for (String item : list) {
            sb.append(item).append(", ");
        }
        sb.deleteCharAt(sb.length() - 1);
        sb.deleteCharAt(sb.length() - 1);
        return sb.toString();
    }
}
