package com.wdf.fudoc.common.constant;

import org.apache.commons.lang.StringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-14 13:54:13
 */
public interface FuConsoleConstants {

    String LINE = "----------------------------------------------------";


    String START = LINE + " [START]";
    String END = LINE + " [END]";

    static String lineContent(String content) {
        if (Objects.isNull(content)) {
            content = StringUtils.EMPTY;
        }
        return " ".repeat((LINE.length() - content.length()) / 2) + content;
    }
}
