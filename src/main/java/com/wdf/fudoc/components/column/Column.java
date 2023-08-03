package com.wdf.fudoc.components.column;

import lombok.Getter;
import lombok.Setter;

import javax.swing.table.TableCellEditor;
import javax.swing.table.TableCellRenderer;

/**
 * @author wangdingfu
 * @date 2022-09-05 16:07:53
 */
@Getter
@Setter
public abstract class Column {

    public abstract Class<?> getColumnClass();

    public abstract String getFieldName();

    public TableCellRenderer getCellRenderer() {
        return null;
    }

    /**
     * 列名
     */
    private String name;

    /**
     * 列编辑器
     */
    private TableCellEditor editor;


    public Column(String name, TableCellEditor editor) {
        this.name = name;
        this.editor = editor;
    }


}
