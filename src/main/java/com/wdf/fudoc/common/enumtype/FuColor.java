package com.wdf.fudoc.common.enumtype;

import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.editor.colors.TextAttributesKey;
import com.intellij.openapi.editor.markup.TextAttributes;
import com.intellij.ui.JBColor;
import com.wdf.fudoc.util.ColorUtils;
import lombok.Getter;

import java.awt.*;
import java.util.Objects;

import static com.intellij.execution.ui.ConsoleViewContentType.LOG_DEBUG_OUTPUT_KEY;

/**
 * @author wangdingfu
 * @date 2022-12-02 21:53:37
 */
@Getter
public enum FuColor {

    GITEE("#fea436", "#fea436", 0),
    GITHUB("#2da44e", "#2da44e", 0),
    ORANGE("#d9480f", "#d9480f", 0),
    DOCUMENT("#0969da", "#0969da", 0),
    RED("#e74c3c", "#e74c3c", 0),
    GREEN("#4a8a5a", "#4a8a5a", 0),

    color1("#6897BB", "#6897BB", 0),
    color2("#D5756C", "#D5756C", 0),
    color3("#629755", "#629755", 0),
    color4("#9876AA", "#9876AA", 0),
    color5("#F75464", "#F75464", 0),
    color6("#56A8F5", "#56A8F5", 0),
    color7("#299999", "#299999", 0),
    color8("#4C5052", "#4C5052", 0),
    color9("#BBBBBB", "#BBBBBB", 0),
    color10("#3C3F41", "#3C3F41", 0),

    console_error("ERROR_OUTPUT", "ERROR_OUTPUT", 1),
    console_info("NORMAL_OUTPUT", "NORMAL_OUTPUT", 1),
    console_user_info("CONSOLE_USER_INPUT", "CONSOLE_USER_INPUT", 1),
    console_warn("LOG_WARNING_OUTPUT", "LOG_WARNING_OUTPUT", 1),
    console_verbose("LOG_VERBOSE_OUTPUT", "LOG_VERBOSE_OUTPUT", 1),
    console_debug("LOG_DEBUG_OUTPUT", "LOG_DEBUG_OUTPUT", 1),


    ;
    /**
     * 常规主题展示的颜色（白色主题）
     */
    private final String regularColor;

    /**
     * 黑色主题展示的颜色
     */
    private final String darkColor;

    /**
     * 颜色类型
     */
    private final int type;

    FuColor(String regularColor, String darkColor, int type) {
        this.regularColor = regularColor;
        this.darkColor = darkColor;
        this.type = type;
    }

    public JBColor color() {
        if (this.type == 0) {
            return new JBColor(ColorUtils.convertColor(getRegularColor()), ColorUtils.convertColor(getDarkColor()));
        } else if (this.type == 1) {
            TextAttributesKey textAttributesKey = TextAttributesKey.createTextAttributesKey(this.regularColor);
            TextAttributes defaultAttributes = textAttributesKey.getDefaultAttributes();
            Color foregroundColor = defaultAttributes.getForegroundColor();
            if(Objects.nonNull(foregroundColor)){
                return new JBColor(foregroundColor, foregroundColor);
            }
        }
        return JBColor.WHITE;
    }
}
