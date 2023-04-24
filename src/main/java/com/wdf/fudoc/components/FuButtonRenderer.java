package com.wdf.fudoc.components;

import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;

import javax.swing.*;
import javax.swing.table.TableCellRenderer;

/**
 * @author wangdingfu
 * @date 2023-01-19 16:37:16
 */
public class FuButtonRenderer implements TableCellRenderer {

    private final JButton button;

    public FuButtonRenderer(JButton button) {
        this.button = button;
        button.setOpaque(true);
    }

    public JComponent getTableCellRendererComponent(JTable table, Object value,
                                                    boolean isSelected, boolean hasFocus, int row, int column) {
        return button;
    }


}
