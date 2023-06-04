package com.wdf.fudoc.common.enumtype;

import com.intellij.ui.JBColor;
import com.wdf.fudoc.util.ColorUtils;
import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2022-12-02 21:53:37
 */
@Getter
public enum FuColor {

    GITEE("#fea436", "#fea436"),
    GITHUB("#2da44e", "#2da44e"),
    ORANGE("#d9480f", "#d9480f"),
    DOCUMENT("#0969da", "#0969da"),
    RED("#e74c3c", "#e74c3c"),
    GREEN("#4a8a5a", "#4a8a5a"),

    color1("#6897BB","#6897BB"),
    color2("#D5756C","#D5756C"),
    color3("#629755","#629755"),
    color4("#9876AA","#9876AA"),
    color5("#F75464","#F75464"),
    color6("#56A8F5","#56A8F5"),
    color7("#299999","#299999"),


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


    public JBColor color() {
        return new JBColor(ColorUtils.convertColor(getRegularColor()), ColorUtils.convertColor(getDarkColor()));
    }
}
