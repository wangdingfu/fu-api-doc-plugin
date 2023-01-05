package com.wdf.fudoc.apidoc.constant.enumtype;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 请求参数类型
 * @author wangdingfu
 * @date 2022-09-07 14:07:13
 */
@Getter
public enum RequestParamType {

    /**
     * 文本
     */
    TEXT("text","文本"),
    /**
     * 文件
     */
    FILE("file","文件"),


    ;

    private final String code;
    private final String view;

    RequestParamType(String code, String view) {
        this.code = code;
        this.view = view;
    }

    public static Set<String> getCodes(){
        return Arrays.stream(RequestParamType.values()).map(RequestParamType::getCode).collect(Collectors.toSet());
    }
}
