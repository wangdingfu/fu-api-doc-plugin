package com.wdf.fudoc.apidoc.constant.enumtype;

import lombok.Getter;

/**
 * 请求数据类型
 *
 * @author wangdingfu
 * @date 2022-05-17 23:33:21
 */
@Getter
public enum ContentType {

    /**
     * POST 请求
     */
    URLENCODED("application/x-www-form-urlencoded", ""),
    FORM_DATA("multipart/form-data", "form"),
    JSON("content-type:application/json", "json"),
    RAW("content-type:application/raw", "raw"),
    ;

    private String type;

    private String desc;

    ContentType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
