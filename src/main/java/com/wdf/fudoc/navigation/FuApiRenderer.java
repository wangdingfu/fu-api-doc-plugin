package com.wdf.fudoc.navigation;

import com.intellij.ide.actions.SearchEverywherePsiRenderer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.speedSearch.SpeedSearchUtil;
import com.intellij.util.ui.UIUtil;
import com.wdf.api.enumtype.FuColor;
import com.wdf.fudoc.util.ColorUtils;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;

/**
 * Api搜索列表渲染
 *
 * @author wangdingfu
 * @date 2023-05-25 18:58:28
 */
@Slf4j
public class FuApiRenderer extends SearchEverywherePsiRenderer {

    public FuApiRenderer(Disposable parent) {
        super();
    }

    @Override
    protected boolean customizeNonPsiElementLeftRenderer(ColoredListCellRenderer renderer, JList list, Object value, int index, boolean selected, boolean hasFocus) {
        Color fgColor = list.getForeground();
        Color bgColor = UIUtil.getListBackground();
        SimpleTextAttributes nameAttributes = new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, fgColor);
        ItemMatchers itemMatchers = getItemMatchers(list, value);
        ApiNavigationItem apiNavigationItem = (ApiNavigationItem) value;
        String name = apiNavigationItem.getUrl();
        String timeStr = apiNavigationItem.getTimeStr();
        String timeView = StringUtils.isBlank(timeStr) ? StringUtils.EMPTY : " (" + timeStr + ")";

        String locationString = "    " + apiNavigationItem.getRightText();

        SpeedSearchUtil.appendColoredFragmentForMatcher(name, renderer, nameAttributes, itemMatchers.nameMatcher, bgColor, selected);
        renderer.setIcon(apiNavigationItem.getRequestType().getIcon());

        if (StringUtils.isNotEmpty(locationString)) {
            FontMetrics fm = list.getFontMetrics(list.getFont());
            int maxWidth = list.getWidth() - fm.stringWidth(name) - myRightComponentWidth - 36;
            int fullWidth = fm.stringWidth(locationString + timeView);
            if (fullWidth >= maxWidth) {
                String text = locationString + timeView;
                int adjustedWidth = Math.max(text.length() * maxWidth / fullWidth - 1, 3);
                locationString = StringUtil.trimMiddle(locationString, adjustedWidth);
            }
            SpeedSearchUtil.appendColoredFragmentForMatcher(locationString, renderer, SimpleTextAttributes.GRAYED_ATTRIBUTES, itemMatchers.nameMatcher, bgColor, selected);
            if (StringUtils.isNotBlank(timeView)) {
                SimpleTextAttributes timeAttributes = new SimpleTextAttributes(SimpleTextAttributes.STYLE_PLAIN, ColorUtils.convertColor(FuColor.GREEN.getDarkColor()));
                SpeedSearchUtil.appendColoredFragmentForMatcher(timeView, renderer, timeAttributes, itemMatchers.nameMatcher, UIUtil.getListBackground(), selected);
            }
        }
        return true;
    }

}
