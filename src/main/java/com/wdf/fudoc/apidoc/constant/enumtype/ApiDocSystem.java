package com.wdf.fudoc.apidoc.constant.enumtype;

import lombok.Getter;

/**
 * 接口文档系统枚举
 *
 * @author wangdingfu
 * @date 2022-12-31 21:42:54
 */
@Getter
public enum ApiDocSystem {


    YAPI("yapi"), SHOW_DOC("showDoc"),

    ;


    private final String code;

    ApiDocSystem(String code) {
        this.code = code;
    }


    public static ApiDocSystem getInstance(String code) {
        for (ApiDocSystem value : ApiDocSystem.values()) {
            if (value.getCode().equals(code)) {
                return value;
            }
        }
        return null;
    }
}
