package com.wdf.fudoc.util;

import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.TableColumn;

/**
 * @author wangdingfu
 * @date 2022-09-06 11:37:18
 */
public class JTableUtils {



    public static void setupCheckboxColumn(@NotNull TableColumn column, int additionalWidth) {
        int columnWidth = new JCheckBox().getPreferredSize().width;
        int checkboxWidth = columnWidth + additionalWidth;
        column.setResizable(false);
        column.setPreferredWidth(checkboxWidth);
        column.setMaxWidth(checkboxWidth);
        column.setMinWidth(columnWidth);
    }
}
