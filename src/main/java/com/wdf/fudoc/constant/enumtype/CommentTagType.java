package com.wdf.fudoc.constant.enumtype;

import com.wdf.fudoc.pojo.data.CommentTagData;
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
    AUTHOR("author"),
    SEE("see"),
    LINK("link"),

    ;


    private final String name;

    CommentTagType(String name) {
        this.name = name;
    }


    public static CommentTagType getEnum(String tagName) {
        for (CommentTagType value : CommentTagType.values()) {
            if (value.getName().endsWith(tagName)) {
                return value;
            }
        }
        return null;
    }
}
