package com.wdf.fudoc.view;

import cn.hutool.core.util.ArrayUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.wdf.fudoc.data.CustomerSettingData;
import com.wdf.fudoc.data.SettingData;
import com.wdf.fudoc.helper.FuTableHelper;
import com.wdf.fudoc.pojo.bo.FilterFieldBO;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.List;
import java.util.Objects;
import java.util.Vector;

/**
 * Fu Doc 基础设置页面
 *
 * @author wangdingfu
 * @date 2022-08-14 21:31:11
 */
public class FuDocGeneralForm {
    private static final Logger LOGGER = Logger.getInstance(FuDocGeneralForm.class);

    /**
     * 根面板(当前配置页面所有的东西都会挂在这个根面板下)
     */
    @Getter
    private JPanel rootPanel;

    /**
     * 过滤java类和java类属性的panel
     */
    private JPanel filterPanel;
    private JPanel validPanel;
    private JPanel enumPanel;

    /**
     * 存放过滤数据的table
     */
    private JTable filterTable;

    /**
     * 项目
     */
    private final Project project;

    /**
     * 持久化数据
     */
    private final SettingData settingData;


    private static final Vector<String> CUSTOM_TITLE = new Vector<>(Lists.newArrayList("类路径", "属性"));


    public FuDocGeneralForm(Project project, SettingData settingData) {
        this.project = project;
        this.settingData = settingData;
    }

    /**
     * 系统会自动调用该方法来完成filterPanel的创建
     */
    private void createUIComponents() {
        List<FilterFieldBO> filterFieldList = getFilterFieldList();
        DefaultTableModel defaultTableModel = new DefaultTableModel(convertTableData(filterFieldList), CUSTOM_TITLE);
        //初始化table
        filterTable = new JBTable(defaultTableModel);
        filterTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(filterTable);
        //添加一行
        toolbarDecorator.setAddAction(button -> {
            //添加一行
            defaultTableModel.addRow(new Vector[]{});
            int maxRow = defaultTableModel.getRowCount() - 1;
            //设置选中刚新增的那一行
            filterTable.setRowSelectionInterval(maxRow, maxRow);
            filterFieldList.add(new FilterFieldBO());
        });
        //移除一行
        toolbarDecorator.setRemoveAction(anActionButton -> {
            //移除一行
            int[] selectedRows = ArrayUtil.reverse(filterTable.getSelectedRows());
            for (int selectedRow : selectedRows) {
                if (selectedRow >= 0) {
                    try {
                        defaultTableModel.removeRow(selectedRow);
                        filterFieldList.remove(selectedRow);
                    } catch (Exception e) {
                        LOGGER.error("从属性过滤表中移除【" + selectedRow + "】行数据失败", e);
                    }
                }
            }
        });
        FuTableHelper.addChangeListener(filterTable, (row, column, beforeValue, afterValue) -> {
            //当单元格的属性被改变后 会调用该方法
            //对过滤数据扩容
            FilterFieldBO filterFieldBO = resize(filterFieldList, row);
            //将table中编辑后的数据设置到持久化数据对象中
            setValue(filterFieldBO, column, afterValue);
        });
        this.filterPanel = toolbarDecorator.createPanel();
    }


    /**
     * 获取需要过滤的属性集合
     */
    private List<FilterFieldBO> getFilterFieldList() {
        CustomerSettingData customerSettingData = settingData.getCustomerSettingData();
        if (Objects.isNull(customerSettingData)) {
            customerSettingData = new CustomerSettingData();
            settingData.setCustomerSettingData(customerSettingData);
        }
        List<FilterFieldBO> filterFieldBOList = customerSettingData.getSettings_filter_field();
        if (Objects.isNull(filterFieldBOList)) {
            filterFieldBOList = Lists.newArrayList();
            customerSettingData.setSettings_filter_field(filterFieldBOList);
        }
        return filterFieldBOList;
    }


    /**
     * 进入该设置页面时会调用该方法
     */
    public void reset() {
        createUIComponents();
    }

    /**
     * 将变更后的值设置到持久化数据对象中
     *
     * @param filterFieldBO 会被持久化的数据
     * @param column        变更单元格的列
     * @param value         变更后的值
     */
    private void setValue(FilterFieldBO filterFieldBO, int column, String value) {
        if (column == 0) {
            filterFieldBO.setClassName(value);
        }
        if (column == 1) {
            filterFieldBO.setFieldNames(value);
        }
    }


    /**
     * 将持久化的数据转换为表格的初始数据
     *
     * @param filterFieldList 被初始化的数据
     * @return 表格需要的数据格式
     */
    private Vector<Vector<String>> convertTableData(List<FilterFieldBO> filterFieldList) {
        // 初始化自定义变量表格
        Vector<Vector<String>> customData = new Vector<>(filterFieldList.size());
        for (FilterFieldBO filterFieldBO : filterFieldList) {
            Vector<String> row = new Vector<>(2);
            row.add(filterFieldBO.getClassName());
            row.add(filterFieldBO.getFieldNames());
            customData.add(row);
        }
        return customData;
    }


    /**
     * 对持久化数据集合扩容
     *
     * @param filterFieldList 会被持久化的数据
     * @param row             当前操作的行
     */
    private FilterFieldBO resize(List<FilterFieldBO> filterFieldList, int row) {
        if (row + 1 > filterFieldList.size()) {
            int resizeLength = row + 1 - filterFieldList.size();
            for (int i = 0; i < resizeLength; i++) {
                filterFieldList.add(new FilterFieldBO());
            }
        }
        FilterFieldBO filterFieldBO = filterFieldList.get(row);
        if (Objects.isNull(filterFieldBO)) {
            filterFieldBO = new FilterFieldBO();
            filterFieldList.add(filterFieldBO);
        }
        return filterFieldBO;
    }
}
