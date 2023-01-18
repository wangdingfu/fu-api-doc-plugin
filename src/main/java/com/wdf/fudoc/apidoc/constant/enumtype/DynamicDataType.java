package com.wdf.fudoc.apidoc.constant.enumtype;

import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2022-08-16 22:45:50
 */
@Getter
public enum DynamicDataType {

    ANNOTATION("annotation","注解"),

    ;

    private final String code;

    private final String msg;

    DynamicDataType(String code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public static Set<String> getCodes(){
        return Arrays.stream(DynamicDataType.values()).map(DynamicDataType::getCode).collect(Collectors.toSet());
    }
}
