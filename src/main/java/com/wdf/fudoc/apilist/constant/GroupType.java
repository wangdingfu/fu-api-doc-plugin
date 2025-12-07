package com.wdf.fudoc.apilist.constant;

import lombok.Getter;

/**
 * API 列表分组类型
 *
 * @author wangdingfu
 * @date 2025-01-12
 */
@Getter
public enum GroupType {

    /**
     * 按模块分组
     */
    MODULE("按 Module 分组", "module"),

    /**
     * 按 URL 前缀分组
     */
    PREFIX("按 URL 前缀分组", "prefix");

    private final String displayName;
    private final String code;

    GroupType(String displayName, String code) {
        this.displayName = displayName;
        this.code = code;
    }

    @Override
    public String toString() {
        return displayName;
    }
}
