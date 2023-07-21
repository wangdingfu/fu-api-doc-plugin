package com.wdf.fudoc.components;

import com.intellij.util.ui.table.IconTableCellRenderer;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2023-07-20 21:57:06
 */
public class FuTableComboBoxRenderer extends IconTableCellRenderer<String> {

    private final Icon icon;

    public FuTableComboBoxRenderer(Icon icon) {
        this.icon = icon;
    }

    @Override
    protected @Nullable Icon getIcon(@NotNull String value, JTable table, int row) {
        return this.icon;
    }

}
