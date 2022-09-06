package com.wdf.fudoc.view.components;

import cn.hutool.core.util.ReflectUtil;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.EditableModel;
import com.wdf.fudoc.factory.FuTableColumnFactory;
import com.wdf.fudoc.util.JTableUtils;
import com.wdf.fudoc.view.bo.Column;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * @author wangdingfu
 * @date 2022-08-16 21:43:46
 */
public class FuTableComponent<T> extends DefaultTableModel implements EditableModel {

    /**
     * table列集合
     */
    private final List<Column> columnList;

    /**
     * table数据集合
     */
    @Getter
    private List<T> dataList;

    /**
     * table对象
     */
    private JBTable jbTable;

    /**
     * table数据的class对象
     */
    private final Class<T> clazz;


    public FuTableComponent(List<Column> columnList, List<T> dataList, Class<T> clazz) {
        this.columnList = columnList;
        this.dataList = dataList;
        this.clazz = clazz;
        this.initTable();
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        return this.columnList.get(columnIndex).getColumnClass();
    }

    /**
     * 新增一行数据
     */
    @Override
    public void addRow() {
        //添加一条默认的空数据
        T data = newInstance();
        //添加到数据集合中(用于持久化)
        this.dataList.add(data);
        //添加到table中
        addRow(data);
    }

    /**
     * 移除一行
     *
     * @param row the row index of the row to be removed
     */
    @Override
    public void removeRow(int row) {
        //从持久化数据对象中移除
        this.dataList.remove(row);
        //从table中移除
        super.removeRow(row);
    }

    /**
     * row移动
     *
     * @param oldIndex 原来的索引
     * @param newIndex 移动后的索引
     */
    @Override
    public void exchangeRows(int oldIndex, int newIndex) {
        //table中移动
        super.moveRow(oldIndex, oldIndex, newIndex);
        //持久化数据集合中移动
        this.dataList.add(newIndex, this.dataList.remove(oldIndex));
    }

    /**
     * 是否可以取消移动
     *
     * @param oldIndex 原来的索引
     * @param newIndex 新的索引
     * @return true 可以
     */
    @Override
    public boolean canExchangeRows(int oldIndex, int newIndex) {
        return true;
    }


    /**
     * 某一个单元格的值变更 需要修改这个值
     *
     * @param value  the new value; this can be null
     * @param row    the row whose value is to be changed
     * @param column the column whose value is to be changed
     */
    @Override
    public void setValueAt(Object value, int row, int column) {
        if (row < this.dataList.size()) {
            //修改table中的值
            super.setValueAt(value, row, column);
            //修改持久化数据集合中的值
            FuTableColumnFactory.setValue(this.dataList.get(row), value, this.columnList.get(column));
        }
    }

    /**
     * 创建一个table组件
     *
     * @param columnList table列
     * @param dataList   table数据
     * @param clazz      table数据class
     * @return 一个已经渲染好了table数据面板 可用于挂在在根面板上直接显示
     */
    public static <T> FuTableComponent<T> create(List<Column> columnList, List<T> dataList, Class<T> clazz) {
        return new FuTableComponent<>(columnList, dataList, clazz);
    }

    /**
     * 创建面板
     */
    public JPanel createPanel() {
        return ToolbarDecorator.createDecorator(this.jbTable).createPanel();
    }


    /**
     * 初始化table
     */
    private void initTable() {
        this.jbTable = new JBTable(this);
        //只支持单选
        this.jbTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //将列初始化到table
        this.columnList.forEach(f -> addColumn(f.getName()));
        //格式化第一列
        formatFirstColumn();
        //设置table单元格编辑器
        this.columnList.stream().filter(f -> Objects.nonNull(f.getEditor())).forEach(f -> this.jbTable.getColumn(f.getName()).setCellEditor(f.getEditor()));
        //将数据添加到table
        setDataList(this.dataList);
    }


    /**
     * 第一列如果是复选框 则重置该列的宽度
     */
    private void formatFirstColumn() {
        Column firstColumn = this.columnList.get(0);
        Class<?> columnClass = firstColumn.getColumnClass();
        if (Objects.nonNull(columnClass) && columnClass == Boolean.class && StringUtils.isBlank(firstColumn.getName())) {
            JTableUtils.setupCheckboxColumn(this.jbTable.getColumnModel().getColumn(0), 30);
            JBTable.setupCheckboxShortcut(this.jbTable, 0);
        }
    }


    /**
     * 设置table数据
     *
     * @param dataList 数据集合
     */
    public void setDataList(List<T> dataList) {
        this.dataList = dataList;
        // 清空数据
        removeAllRow();
        for (T entity : this.dataList) {
            addRow(entity);
        }
    }

    /**
     * 清空table数据
     */
    private void removeAllRow() {
        //获取总条数
        int rowCount = getRowCount();
        for (int i = 0; i < rowCount; i++) {
            removeRow(i);
        }
    }


    /**
     * 添加一行数据
     *
     * @param data 数据对象
     */
    private void addRow(T data) {
        super.addRow(toTableData(data));
    }

    /**
     * 将数据转换为table数据格式
     *
     * @param data 数据对象
     * @return table中的一行数据
     */
    private Vector<Object> toTableData(T data) {
        Vector<Object> vector = new Vector<>();
        this.columnList.stream().map(item -> FuTableColumnFactory.getValue(data, item)).forEach(vector::add);
        return vector;
    }

    /**
     * 实例化表格数据对象
     */
    private T newInstance() {
        return ReflectUtil.newInstance(this.clazz);
    }

}
