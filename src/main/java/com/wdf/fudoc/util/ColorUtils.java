package com.wdf.fudoc.util;

import com.intellij.ui.ColorHexUtil;
import com.intellij.ui.JBColor;
import com.intellij.util.ui.UIUtil;
import cn.fudoc.common.msg.bo.FuMsgItemBO;

import java.awt.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-12-02 21:46:14
 */
public class ColorUtils {


    public static JBColor getColor(FuMsgItemBO fuMsgItemBO) {
        if (Objects.nonNull(fuMsgItemBO)) {
            String regularColor = fuMsgItemBO.getRegularColor();
            String darkColor = fuMsgItemBO.getDarkColor();
            if (FuStringUtils.isNotBlank(regularColor) || FuStringUtils.isNotBlank(darkColor)) {
                return new JBColor(convertColor(regularColor), convertColor(darkColor));
            }
        }
        return null;
    }


    public static Color convertColor(String color) {
        return FuStringUtils.isBlank(color) ? UIUtil.getLabelBackground() : ColorHexUtil.fromHex(color);
    }

}
