package com.wdf.fudoc.apidoc.sync.renderer;

import com.intellij.ui.JBColor;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.listener.FuTableListener;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;

/**
 * @author wangdingfu
 * @date 2023-07-12 23:14:57
 */
public class DisableCellRenderer<T> extends DefaultTableCellRenderer {

    private final FuTableListener<T> fuTableListener;

    public DisableCellRenderer(FuTableListener<T> fuTableListener) {
        this.fuTableListener = fuTableListener;
    }

    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component component = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        if (isSelected) {
            return component;
        }
        if (fuTableListener.isCellEditable(row, column)) {
            component.setBackground(JBUI.CurrentTheme.Table.background(false, false));
        } else {
            setForeground(JBUI.CurrentTheme.Table.foreground(false, true));
            setBackground(JBUI.CurrentTheme.Table.background(false, true));
        }
        return component;
    }

}
