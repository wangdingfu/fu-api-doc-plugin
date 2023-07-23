package com.wdf.fudoc.components;

import cn.hutool.core.util.ReflectUtil;
import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.intellij.util.ui.EditableModel;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.column.Column;
import com.wdf.fudoc.components.column.DynamicColumn;
import com.wdf.fudoc.components.dialog.CustomTableSettingDialog;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.util.JTableUtils;
import com.wdf.fudoc.util.ProjectUtils;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import javax.swing.table.TableColumn;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Vector;
import java.util.stream.Collectors;

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
    private final List<T> dataList;

    /**
     * table对象
     */
    private FuTableView<T> fuTableView;

    /**
     * table数据的class对象
     */
    private final Class<T> clazz;

    /**
     * table组件监听器
     */
    @Getter
    private FuTableListener<T> fuTableListener;

    /**
     * 表格key标识 如果设置则表示当前表头可以自定义配置
     */
    private final String tableKey;

    /**
     * 自定义动作列表
     */
    private final List<AnAction> actionList = Lists.newArrayList();

    public List<T> getDataList() {
        return Objects.isNull(dataList) ? Lists.newArrayList() : dataList;
    }

    public FuTableComponent(String tableKey, List<Column> columnList, List<T> dataList, Class<T> clazz, FuTableListener<T> fuTableListener) {
        this.tableKey = tableKey;
        this.fuTableListener = fuTableListener;
        this.columnList = columnList;
        this.dataList = dataList;
        this.clazz = clazz;
        this.initTable();
    }


    public void setEnable(boolean enable) {
        if (Objects.nonNull(this.fuTableView)) {
            this.fuTableView.setEnabled(enable);
        }
    }

    public FuTableComponent<T> addAnAction(AnAction anAction) {
        this.actionList.add(anAction);
        return this;
    }


    public void addListener(FuTableListener<T> fuTableListener) {
        this.fuTableListener = fuTableListener;
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        if (columnIndex < this.columnList.size()) {
            Column column = this.columnList.get(columnIndex);
            if (Objects.nonNull(column)) {
                return column.getColumnClass();
            }
        }
        return String.class;
    }

    /**
     * 新增一行数据
     */
    @Override
    public void addRow() {
        T data = (Objects.nonNull(fuTableListener) && fuTableListener.customerAddData()) ? fuTableListener.addData() : newInstance();
        if (Objects.isNull(data)) {
            return;
        }
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
        boolean isCanDelete = true;
        if (Objects.nonNull(fuTableListener)) {
            isCanDelete = fuTableListener.isCanDelete(this.dataList.get(row));
        }
        if (isCanDelete) {
            removeRowByIndex(row);
            if (Objects.nonNull(fuTableListener)) {
                fuTableListener.deleteRow(row);
            }
        }
    }


    public void removeColumn(String columnName) {
        super.columnIdentifiers.removeElement(columnName);
        justifyRows(0, getRowCount());
        fireTableStructureChanged();
    }

    private void justifyRows(int from, int to) {
        dataVector.setSize(getRowCount());
        for (int i = from; i < to; i++) {
            if (dataVector.elementAt(i) == null) {
                dataVector.setElementAt(new Vector<>(), i);
            }
            dataVector.elementAt(i).setSize(getColumnCount());
        }
    }

    public void removeRowByIndex(int row) {
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
        if (canExchangeRows(oldIndex, newIndex)) {
            //table中移动
            super.moveRow(oldIndex, oldIndex, newIndex);
            //持久化数据集合中移动
            this.dataList.add(newIndex, this.dataList.remove(oldIndex));
            if (Objects.nonNull(this.fuTableListener)) {
                this.fuTableListener.exchangeRows(oldIndex, newIndex);
            }
        }
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
        if (Objects.nonNull(this.fuTableListener)) {
            return this.fuTableListener.canExchangeRows(oldIndex, newIndex);
        }
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
            T data = this.dataList.get(row);
            FuTableColumnFactory.setValue(data, value, this.columnList.get(column));
            if (Objects.nonNull(this.fuTableListener)) {
                this.fuTableListener.propertyChange(data, row, column, value);
            }
        }
    }


    public static FuTableComponent<KeyValueTableBO> createKeyValue() {
        return create(FuTableColumnFactory.keyValueColumns(), KeyValueTableBO.class);
    }

    public static FuTableComponent<KeyValueTableBO> createKeyValueFile() {
        return create(FuTableColumnFactory.formDataColumns(), KeyValueTableBO.class);
    }

    public static <T> FuTableComponent<T> create(String tableKey, List<Column> columnList, Class<T> clazz, FuTableListener<T> fuTableListener) {
        return new FuTableComponent<>(tableKey, columnList, Lists.newArrayList(), clazz, fuTableListener);
    }

    public static <T> FuTableComponent<T> create(List<Column> columnList, Class<T> clazz) {
        return create(null, columnList, clazz, null);
    }

    public static <T> FuTableComponent<T> create(String tableKey, List<Column> columnList, Class<T> clazz) {
        return create(tableKey, columnList, clazz, null);
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
        return new FuTableComponent<>(null, columnList, dataList, clazz, null);
    }


    /**
     * 创建面板
     */
    public JPanel createPanel() {
        if (Objects.isNull(this.fuTableView)) {
            return new JPanel();
        }
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(this.fuTableView);
        if (StringUtils.isNotBlank(this.tableKey)) {
            decorator.addExtraActions(new AnAction("Settings", "", AllIcons.General.Settings) {
                @Override
                public void actionPerformed(@NotNull AnActionEvent e) {
                    CustomTableSettingDialog customTableSettingDialog = new CustomTableSettingDialog(e.getProject(), tableKey, columnToDataList());
                    if (customTableSettingDialog.showAndGet()) {
                        //重置表格
                        resetColumn();
                        //初始化表格的列
                        initTableColumn();

                    }
                }
            });
        }
        if (CollectionUtils.isNotEmpty(this.actionList)) {
            decorator.addExtraActions(this.actionList.toArray(new AnAction[0]));
        }
        return decorator.createPanel();
    }


    private List<KeyValueTableBO> columnToDataList() {
        return columnList.stream().map(m -> new KeyValueTableBO(true, m.getFieldName(), m.getName(), m instanceof DynamicColumn)).collect(Collectors.toList());
    }


    private void resetColumn() {
        columnList.forEach(f -> removeColumn(f.getName()));
        columnList.removeIf(f -> f instanceof DynamicColumn);
        int rowCount = super.getRowCount();
        for (int i = rowCount - 1; i >= 0; i--) {
            super.removeRow(i);
        }
    }


    public JPanel createMainPanel() {
        if (Objects.isNull(this.fuTableView)) {
            return new JPanel();
        }
        ToolbarDecorator decorator = ToolbarDecorator.createDecorator(this.fuTableView);
        decorator.disableAddAction();
        decorator.disableRemoveAction();
        decorator.disableUpAction();
        decorator.disableDownAction();
        return decorator.createPanel();
    }

    /**
     * 初始化table
     */
    private void initTable() {
        this.fuTableView = new FuTableView<>(this);
        //只支持单选
        this.fuTableView.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        //设置table渲染器
        if (Objects.nonNull(this.fuTableListener) && Objects.nonNull(this.fuTableListener.getTableCellRenderer())) {
            this.fuTableView.setDefaultRenderer(Object.class, this.fuTableListener.getTableCellRenderer());
        }
        initTableColumn();
    }


    private void initTableColumn() {
        //初始化columnList
        initColumn();
        //将列初始化到table
        this.columnList.forEach(f -> addColumn(f.getName()));
        //格式化第一列
        formatFirstColumn();
        //设置table单元格编辑器
        this.columnList.stream().filter(f -> Objects.nonNull(f.getEditor())).forEach(f -> {
            TableColumn column = this.fuTableView.getColumn(f.getName());
            column.setCellEditor(f.getEditor());
            TableCellRenderer cellRenderer = f.getCellRenderer();
            if (Objects.nonNull(cellRenderer)) {
                column.setCellRenderer(cellRenderer);
            }
        });
        //将数据添加到table
        if (CollectionUtils.isNotEmpty(this.dataList)) {
            this.dataList.forEach(this::addRow);
        }
    }


    public void initColumn() {
        if (StringUtils.isBlank(this.tableKey)) {
            return;
        }
        FuRequestConfigPO fuRequestConfigPO = FuRequestConfigStorage.get(ProjectUtils.getCurrProject()).readData();
        Map<String, List<KeyValueTableBO>> customTableConfigMap = fuRequestConfigPO.getCustomTableConfigMap();
        List<KeyValueTableBO> keyValueTableBOList = customTableConfigMap.get(this.tableKey);
        if (CollectionUtils.isEmpty(keyValueTableBOList)) {
            return;
        }
        keyValueTableBOList.forEach(f -> this.columnList.add(new DynamicColumn(f.getValue(), f.getKey())));
    }


    @Override
    public boolean isCellEditable(int row, int column) {
        if (Objects.nonNull(this.fuTableListener)) {
            return this.fuTableListener.isCellEditable(row, column);
        }
        return super.isCellEditable(row, column);
    }

    /**
     * 第一列如果是复选框 则重置该列的宽度
     */
    private void formatFirstColumn() {
        Column firstColumn = this.columnList.get(0);
        Class<?> columnClass = firstColumn.getColumnClass();
        if (Objects.nonNull(columnClass) && columnClass == Boolean.class && StringUtils.isBlank(firstColumn.getName())) {
            JTableUtils.setupCheckboxColumn(this.fuTableView.getColumnModel().getColumn(0), 30);
            JBTable.setupCheckboxShortcut(this.fuTableView, 0);
        }
    }


    /**
     * 设置table数据
     *
     * @param dataList 数据集合
     */
    public void setDataList(List<T> dataList) {
        // 清空数据
        removeAllRow();
        //将新的数据添加到表格中
        dataList.forEach(this::addRowData);
    }

    /**
     * 清空table数据
     */
    private void removeAllRow() {
        //获取总条数
        int rowCount = getRowCount() - 1;
        //逆向移除
        for (int i = rowCount; i >= 0; i--) {
            removeRowByIndex(i);
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
     * 添加一行数据到table组件中
     *
     * @param data table中的一条数据
     */
    public void addRowData(T data) {
        this.dataList.add(data);
        addRow(data);
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
     * 获取表格中某一行的数据
     *
     * @param row 行数
     * @return 当前行的数据
     */
    public T getData(Integer row) {
        if (Objects.nonNull(row) && this.dataList.size() > row) {
            return this.dataList.get(row);
        }
        return null;
    }


    /**
     * 实例化表格数据对象
     */
    private T newInstance() {
        return ReflectUtil.newInstance(this.clazz);
    }

}
