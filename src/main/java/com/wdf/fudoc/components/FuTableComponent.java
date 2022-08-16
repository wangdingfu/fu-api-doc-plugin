package com.wdf.fudoc.components;

import cn.hutool.core.lang.Dict;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.ReflectUtil;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.wdf.fudoc.constant.FuTableConstants;
import com.wdf.fudoc.exception.FuDocException;
import com.wdf.fudoc.helper.FuTableHelper;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableColumn;
import java.util.*;

/**
 * @author wangdingfu
 * @date 2022-08-15 14:01:56
 */
public class FuTableComponent<T> {

    /**
     * 表格的标题集合
     */
    private final Vector<String> titles;

    /**
     * 表格持久化数据集合
     */
    @Getter
    private final List<T> dataList;

    /**
     * 表格每一列数据的对象class
     */
    private final Class<T> clazz;

    /**
     * 存储表格title和对象字段关系的字典
     */
    private final Dict dict;

    /**
     * 表格对象
     */
    private JTable jTable;

    /**
     * 表格的model
     */
    private DefaultTableModel defaultTableModel;

    /**
     * 工具栏装饰器(通过这个装饰器把表格装饰了一遍 为表格新增了工具栏上的操作)
     */
    private ToolbarDecorator toolbarDecorator;

    private static final Logger LOGGER = Logger.getInstance(FuTableComponent.class);

    /**
     * 选中模式 默认支持多选
     */
    @Setter
    private int selectionMode = ListSelectionModel.MULTIPLE_INTERVAL_SELECTION;


    public FuTableComponent(Dict dict, List<T> dataList, Class<T> clazz) {
        if (Objects.isNull(dict)) {
            throw new FuDocException("【Fu Table】初始化失败. 无法获取到表格标题");
        }
        this.dataList = dataList;
        this.dict = dict;
        this.clazz = clazz;
        this.titles = new Vector<>(dict.keySet());
        //初始化table
        initTable();
    }


    /**
     * 创建一个table组件实例对象
     *
     * @param key      可以根据这个key获取到table的标题和映射到对象的字段名
     * @param dataList table的数据集合
     * @param <T>      table数据格式
     * @return 返回创建好的table组件
     */
    public static <T> JPanel createComponent(String key, List<T> dataList, Class<T> clazz) {
        return new FuTableComponent<>(FuTableConstants.get(key), dataList, clazz).createPanel();
    }


    /**
     * 创建一个还没有定义好的table
     * 默认有新增和删除的操作
     *
     * @param key      可以根据这个key获取到table的标题和映射到对象的字段名
     * @param dataList table的数据集合
     * @param <T>      table数据格式
     * @return 返回创建好的table组件
     */
    public static <T> FuTableComponent<T> create(String key, List<T> dataList, Class<T> clazz) {
        return new FuTableComponent<>(FuTableConstants.get(key), dataList, clazz);
    }


    /**
     * 初始化table
     */
    private void initTable() {
        //初始化table
        this.defaultTableModel = new DefaultTableModel(convertTableData(this.dataList), this.titles);
        //初始化table
        this.jTable = new JBTable(this.defaultTableModel);
        //设置选中模式
        this.jTable.setSelectionMode(this.selectionMode);
        //创建装饰器对象
        this.toolbarDecorator = ToolbarDecorator.createDecorator(this.jTable);
        //默认为table添加新增操作
        setAddAction();
        //默认为table添加删除操作
        setRemoveAction();
        //默认为table添加属性变更事件
        setPropertyChangeAction();
    }

    /**
     * 通过装饰器创建table所属面板
     */
    public JPanel createPanel() {
        return this.toolbarDecorator.createPanel();
    }


    /**
     * 设置新增动作
     */
    public void setAddAction() {
        this.toolbarDecorator.setAddAction(anActionButton -> {
            //添加一行
            defaultTableModel.addRow(new Vector[]{});
            int maxRow = defaultTableModel.getRowCount() - 1;
            //设置选中刚新增的那一行
            this.jTable.setRowSelectionInterval(maxRow, maxRow);
            //持久化数据新增
            this.dataList.add(newInstance());
        });
    }


    /**
     * 设置移除动作
     */
    public void setRemoveAction() {
        this.toolbarDecorator.setRemoveAction(anActionButton -> {
            //获取选中的行 并对选中的行进行逆序（由于获取选中的行都是从小到大 但实际移除时 需要按照从大到小的顺序从数组中移除数据 因为是根据下标移除的 先移除小的会影响大的下标移除 ）
            int[] selectedRows = ArrayUtil.reverse(this.jTable.getSelectedRows());
            for (int selectedRow : selectedRows) {
                if (selectedRow >= 0) {
                    try {
                        defaultTableModel.removeRow(selectedRow);
                        this.dataList.remove(selectedRow);
                    } catch (Exception e) {
                        LOGGER.error("从[Fu Table]中移除【" + selectedRow + "】行数据失败", e);
                    }
                }
            }
        });
    }


    /**
     * 为table属性变更监听器
     */
    public void setPropertyChangeAction() {
        FuTableHelper.addChangeListener(this.jTable, (row, column, beforeValue, afterValue) -> {
            //当单元格的属性被改变后 会调用该方法
            //对持久化数据集合扩容(防止页面条数和持久化数据集合条数不对应)
            T data = resize(this.dataList, row);
            //将table中编辑后的数据设置到持久化数据对象中
            setPropertyValue(data, column, afterValue);
        });
    }


    /**
     * 设置表格列宽
     *
     * @param column       列
     * @param widthPercent 宽度百分比
     */
    public FuTableComponent<T> setColumnWidth(int column, double widthPercent) {
        //整个table的宽度
        int tableWidth = this.jTable.getWidth();
        TableColumn tableColumn = this.jTable.getColumnModel().getColumn(column);
        if (Objects.nonNull(tableColumn)) {
            tableColumn.setPreferredWidth(Double.valueOf(tableWidth * widthPercent).intValue());
        }
        return this;
    }

    /**
     * 设置属性值
     *
     * @param data       table中指定某一行的持久化数据对象
     * @param column     table中指定某一列的索引
     * @param afterValue 需要将该数据设置到table中指定单元格内
     */
    private void setPropertyValue(T data, int column, String afterValue) {
        if (titles.size() < column) {
            return;
        }
        //将值通过反射的方式设置到持久化数据对象中
        ReflectUtil.setFieldValue(data, dict.getStr(titles.get(column)), afterValue);
    }


    /**
     * 对持久化数据集合扩容
     *
     * @param dataList 表格里持久化的数据集合
     * @param row      当前操作的行
     */
    private T resize(List<T> dataList, int row) {
        if (row + 1 > dataList.size()) {
            int resizeLength = row + 1 - dataList.size();
            for (int i = 0; i < resizeLength; i++) {
                dataList.add(newInstance());
            }
        }
        T data = dataList.get(row);
        if (Objects.isNull(data)) {
            data = newInstance();
            dataList.add(data);
        }
        return data;
    }

    /**
     * 将持久化的数据转换为表格的初始数据
     *
     * @param dataList 表格初始化的数据集合
     * @return 表格需要的数据格式
     */
    private Vector<Vector<String>> convertTableData(List<T> dataList) {
        // 初始化自定义变量表格
        Vector<Vector<String>> customData = new Vector<>(dataList.size());
        for (T data : dataList) {
            Vector<String> row = new Vector<>(titles.size());
            for (String title : titles) {
                String fieldName = dict.getStr(title);
                if (StringUtils.isNotBlank(fieldName)) {
                    Object fieldValue = ReflectUtil.getFieldValue(data, fieldName);
                    row.add(Objects.isNull(fieldValue) ? StringUtils.EMPTY : fieldValue.toString());
                }
            }
            customData.add(row);
        }
        return customData;
    }


    /**
     * 实例化表格数据对象
     */
    private T newInstance() {
        return ReflectUtil.newInstance(clazz);
    }

}
