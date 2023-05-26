package com.wdf.fudoc.navigation;

import com.intellij.icons.AllIcons;
import com.intellij.ide.actions.SearchEverywherePsiRenderer;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.psi.PsiElement;
import com.intellij.ui.ColoredListCellRenderer;
import com.intellij.ui.SimpleTextAttributes;
import com.intellij.ui.speedSearch.SpeedSearchUtil;
import com.intellij.util.TextWithIcon;
import com.intellij.util.ui.UIUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * Api搜索列表渲染
 *
 * @author wangdingfu
 * @date 2023-05-25 18:58:28
 */
@Slf4j
public class FuApiRenderer extends SearchEverywherePsiRenderer {

    public FuApiRenderer(Disposable parent) {
        super(parent);
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

        String locationString = "    " + apiNavigationItem.getRightText();
        if (StringUtils.isNotBlank(timeStr)) {
            locationString += "   (" + timeStr + ")";
        }

        SpeedSearchUtil.appendColoredFragmentForMatcher(name, renderer, nameAttributes, itemMatchers.nameMatcher, bgColor, selected);
        renderer.setIcon(apiNavigationItem.getRequestType().getIcon());

        if (StringUtils.isNotEmpty(locationString)) {
            FontMetrics fm = list.getFontMetrics(list.getFont());
            int maxWidth = list.getWidth() - fm.stringWidth(name) - myRightComponentWidth - 36;
            int fullWidth = fm.stringWidth(locationString);
            if (fullWidth < maxWidth) {
                SpeedSearchUtil.appendColoredFragmentForMatcher(locationString, renderer, SimpleTextAttributes.GRAYED_ATTRIBUTES, itemMatchers.nameMatcher, bgColor, selected);
            } else {
                int adjustedWidth = Math.max(locationString.length() * maxWidth / fullWidth - 1, 3);
                locationString = StringUtil.trimMiddle(locationString, adjustedWidth);
                SpeedSearchUtil.appendColoredFragmentForMatcher(locationString, renderer, SimpleTextAttributes.GRAYED_ATTRIBUTES, itemMatchers.nameMatcher, bgColor, selected);
            }
        }
        return true;
    }


    @Override
    protected @Nullable TextWithIcon getItemLocation(Object value) {
        if (value instanceof ApiNavigationItem apiNavigationItem) {
            PsiElement psiElement = apiNavigationItem.getPsiElement();
            Module module = ModuleUtil.findModuleForPsiElement(psiElement);
            if (Objects.nonNull(module)) {
                return new TextWithIcon(module.getName(), AllIcons.Nodes.Module);
            }

        }
        return super.getItemLocation(value);
    }
}
