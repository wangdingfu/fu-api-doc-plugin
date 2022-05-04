package com.wdf.apidoc.constant.enumtype;

import lombok.Getter;

/**
 * @author wangdingfu
 * @description 注释tag类型
 * @Date 2022-04-21 17:54:32
 */
@Getter
public enum CommentTagType {

    PARAM("param"),
    RETURN("return"),
    DESCRIPTION("description"),
    AUTHOR("author");


    private final String name;

    CommentTagType(String name) {
        this.name = name;
    }
}
