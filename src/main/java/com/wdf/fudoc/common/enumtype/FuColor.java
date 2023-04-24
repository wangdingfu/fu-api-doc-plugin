package com.wdf.fudoc.common.enumtype;

import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2022-12-02 21:53:37
 */
@Getter
public enum FuColor {

    GITEE("#fea436","#fea436"),
    GITHUB("#2da44e","#2da44e"),
    ORANGE("#d9480f","#d9480f"),
    DOCUMENT("#0969da","#0969da"),
    RED("#e74c3c","#e74c3c"),
    GREEN("#4a8a5a","#4a8a5a"),


    ;
    /**
     * 常规主题展示的颜色（白色主题）
     */
    private final String regularColor;

    /**
     * 黑色主题展示的颜色
     */
    private final String darkColor;

    FuColor(String regularColor, String darkColor) {
        this.regularColor = regularColor;
        this.darkColor = darkColor;
    }
}
