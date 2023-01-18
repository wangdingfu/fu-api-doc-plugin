package com.wdf.fudoc.apidoc.constant.enumtype;

import lombok.Getter;

/**
 * mock数据结果类型
 *
 * @author wangdingfu
 * @date 2022-11-16 23:04:42
 */
@Getter
public enum MockResultType {


    DEFAULT(""),
    JSON("json"),
    YAML("yaml"),
    PROPERTIES("properties");

    private final String code;

    MockResultType(String code) {
        this.code = code;
    }
}
