package com.wdf.fudoc.view.bo;

import lombok.Getter;
import lombok.Setter;

import javax.swing.table.TableCellEditor;

/**
 * @author wangdingfu
 * @date 2022-09-05 16:07:53
 */
@Getter
@Setter
public abstract class Column {

    public abstract Class<?> getColumnClass();

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
