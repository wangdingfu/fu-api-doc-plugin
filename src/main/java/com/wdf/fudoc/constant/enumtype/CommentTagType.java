package com.wdf.fudoc.constant.enumtype;

import lombok.Getter;

import java.util.Objects;

/**
 * @author wangdingfu
 * @description 注释tag类型
 * @date 2022-04-21 17:54:32
 */
@Getter
public enum CommentTagType {

    PARAM("param", 2),
    RETURN("return", 1),
    DESCRIPTION1("description", 1),
    DESCRIPTION2("Description", 1),
    AUTHOR1("author", 1),
    AUTHOR2("Author", 1),
    DATE1("date", 1),
    DATE2("Date", 1),
    SEE("see", 3),
    LINK("link", 3),

    ;


    private final String name;

    /**
     * 1:该tag后面跟随的是内容
     * 2:后面跟随的是key value格式
     * 3:后面跟随的是类、方法、字段的引用
     */
    private final int type;

    CommentTagType(String name, int type) {
        this.name = name;
        this.type = type;
    }

    public static CommentTagType getEnum(String tagName) {
        for (CommentTagType value : CommentTagType.values()) {
            if (value.getName().endsWith(tagName)) {
                return value;
            }
        }
        return null;
    }


    public static boolean hasValue(Integer type) {
        return !Objects.isNull(type) && type == 1;
    }

    public static boolean hasKey(Integer type){
        return !Objects.isNull(type) && type == 2;
    }

    public static boolean hasReference(Integer type){
        return !Objects.isNull(type) && type == 3;
    }
}
