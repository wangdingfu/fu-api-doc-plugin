package com.wdf.fudoc.apidoc.constant.enumtype;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-07-01 21:27:10
 */
@Getter
@AllArgsConstructor
public enum HeaderLevel {

    API("0","接口请求头"),
    GLOBAL("1","全局请求头"),

    ;

    private final String level;

    private final String view;

    public static Set<String> getCodes(){
        return Arrays.stream(HeaderLevel.values()).map(HeaderLevel::getView).collect(Collectors.toSet());
    }
}
