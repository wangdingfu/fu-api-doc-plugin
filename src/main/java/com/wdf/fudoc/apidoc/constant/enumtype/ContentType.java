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
     * GET 请求 不存在content-type属性
     */
    GET("get", ""),
    PATH_VARIABLE("path_variable", ""),

    /**
     * POST 请求
     */
    URLENCODED("application/x-www-form-urlencoded", ""),
    FORM_DATA("multipart/form-data", ""),
    JSON("content-type:application/json", "");

    private String type;

    private String desc;

    ContentType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
