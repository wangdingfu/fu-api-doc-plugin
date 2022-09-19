package com.wdf.fudoc.components.listener;

import com.wdf.fudoc.components.FuTableView;

import javax.swing.table.TableCellEditor;

/**
 * 【Fu Table】组件监听器
 *
 * @author wangdingfu
 * @date 2022-09-19 10:58:33
 */
public interface FuTableListener<T> {


    /**
     * 获取单元格编辑器
     *
     * @param fuTableView 表格对象
     * @param row         行
     * @param column      列
     * @return 指定单元格的编辑器
     */
    default TableCellEditor getCellEditor(FuTableView<T> fuTableView, int row, int column) {
        return null;
    }

    /**
     * table中属性变更
     *
     * @param data   变更那一行的数据对象
     * @param row    变更单元格的行
     * @param column 变更单元格的列
     * @param value  变更后的值
     */
    default void propertyChange(T data, int row, int column, Object value) {

    }


}
