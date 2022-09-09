package com.wdf.fudoc.common.components;

import javax.swing.table.TableCellEditor;

/**
 * @author wangdingfu
 * @date 2022-09-07 20:24:02
 */
public interface FuTableCellEditorListener {

    /**
     * 获取单元格编辑器
     *
     * @param fuTableView 表格对象
     * @param row         行
     * @param column      列
     * @return 指定单元格的编辑器
     */
    TableCellEditor getCellEditor(FuTableView fuTableView, int row, int column);
}
