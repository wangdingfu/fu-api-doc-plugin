package com.wdf.fudoc.apidoc.constant.enumtype;

import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2022-08-15 20:35:51
 */
@Getter
public enum ValidMessageType {

    /**
     * 替换指定内容
     */
    REPLACE(1, "replace", "不能为空,not null"),

    ;

    private final int code;

    private final String msg;

    private final String defaultValue;

    ValidMessageType(int code, String msg, String defaultValue) {
        this.code = code;
        this.msg = msg;
        this.defaultValue = defaultValue;
    }
}
