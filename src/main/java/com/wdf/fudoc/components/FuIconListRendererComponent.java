package com.wdf.fudoc.components;

import com.intellij.icons.AllIcons;

import javax.swing.*;
import java.awt.*;

/**
 * @author wangdingfu
 * @date 2023-06-09 22:57:21
 */
public class FuIconListRendererComponent extends DefaultListCellRenderer {

    private final Icon icon;

    public FuIconListRendererComponent(Icon icon) {
        this.icon = icon;
    }

    @Override
    public Component getListCellRendererComponent(JList<?> list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
        JLabel jLabel = (JLabel) super.getListCellRendererComponent(list, value, index, isSelected, cellHasFocus);
        jLabel.setIcon(icon);
        return jLabel;
    }
}
