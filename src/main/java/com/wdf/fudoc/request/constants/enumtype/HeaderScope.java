package com.wdf.fudoc.request.constants.enumtype;

import com.google.common.collect.Sets;
import lombok.Getter;

import java.util.Set;

/**
 * @author wangdingfu
 * @date 2022-12-07 22:18:08
 */
@Getter
public enum HeaderScope {

    ALL_PROJECT("所有项目"), CURRENT_PROJECT("当前项目"),
    /**
     * TODO 当前暂未实现
     */
    SELECT_MODULE("指定模块"),
    ;


    private final String name;

    HeaderScope(String name) {
        this.name = name;
    }


    public static Set<String> scopeList() {
        return Sets.newHashSet(ALL_PROJECT.getName(), CURRENT_PROJECT.getName());
    }
}
