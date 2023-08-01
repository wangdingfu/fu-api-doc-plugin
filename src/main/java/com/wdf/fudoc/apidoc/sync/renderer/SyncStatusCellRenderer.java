package com.wdf.fudoc.apidoc.sync.renderer;

import com.intellij.ui.JBColor;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiSyncStatus;

import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import java.awt.*;
import java.util.Objects;

/**
 * 同步状态单元格渲染
 *
 * @author wangdingfu
 * @date 2023-01-28 17:26:37
 */
public class SyncStatusCellRenderer extends DefaultTableCellRenderer {


    @Override
    public Component getTableCellRendererComponent(JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        Component c = super.getTableCellRendererComponent(table, value, isSelected, hasFocus, row, column);
        ApiSyncStatus syncStatus;
        if (column == 4 && Objects.nonNull(value) && Objects.nonNull(syncStatus = ApiSyncStatus.getInstance(value.toString()))) {
            switch (syncStatus) {
                case SUCCESS:
                    c.setForeground(JBColor.GREEN);
                    break;
                case FAIL:
                    c.setForeground(JBColor.RED);
                    break;
            }
        }
        return c;
    }
}
