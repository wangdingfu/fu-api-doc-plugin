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

    /**
     * 添加一行
     *
     * @param data 添加的数据
     */
    default void addRow(T data) {

    }

    /**
     * 删除一行
     *
     * @param row 删除的行数
     */
    default void deleteRow(int row) {

    }


    default boolean isCanDelete(T data) {
        return true;
    }

    /**
     * row移动
     *
     * @param oldIndex 原来的索引
     * @param newIndex 移动后的索引
     */
    default void exchangeRows(int oldIndex, int newIndex) {

    }

    default boolean customerAddData() {
        return false;
    }

    /**
     * 新增数据时调用 业务可以自己弹框输入内容后将数据返回到table中
     *
     * @return 默认返回null 新增一条空数据
     */
    default T addData() {
        return null;
    }


    /**
     * 默认可编辑
     */
    default boolean isCellEditable(int rowIndex, int columnIndex) {
        return true;
    }

}
