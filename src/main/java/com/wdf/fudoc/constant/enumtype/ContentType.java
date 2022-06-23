package com.wdf.fudoc.constant.enumtype;

/**
 * @author wangdingfu
 * @descption: 请求数据类型
 * @date 2022-05-17 23:33:21
 */
public enum ContentType {


    /**
     * 正常GET请求
     */
    URLENCODED("application/x-www-form-urlencoded",""),
    /**
     * GET请求  参数需要替换url PathVariable
     */
    URLENCODED1("application/x-www-form-urlencoded",""),
    FORM_DATA("multipart/form-data",""),
    JSON("content-type:application/json","")
    ;

    private String type;

    private String desc;

    ContentType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
