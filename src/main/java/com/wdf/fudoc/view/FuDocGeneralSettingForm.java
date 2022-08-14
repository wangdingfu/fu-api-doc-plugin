package com.wdf.fudoc.view;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.ui.ToolbarDecorator;
import com.intellij.ui.table.JBTable;
import com.wdf.fudoc.FuDocMessageBundle;
import com.wdf.fudoc.config.DefaultConfig;
import com.wdf.fudoc.config.state.FuDocSetting;
import com.wdf.fudoc.constant.MessageConstants;
import com.wdf.fudoc.data.CustomerSettingData;
import com.wdf.fudoc.data.SettingData;
import com.wdf.fudoc.helper.FuTableHelper;
import com.wdf.fudoc.pojo.bo.FilterFieldBO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.util.*;
import java.util.List;

/**
 * @author wangdingfu
 * @date 2022-08-07 23:33:30
 */
@Getter
@Setter
public class FuDocGeneralSettingForm {
    private JPanel root;
    private JLabel label1;
    private JPanel panel1;
    private JTable table1;
    private Project project;
    private JTable customTable;

    private static final Vector<String> CUSTOM_TITLE = new Vector<>(Lists.newArrayList(FuDocMessageBundle.message(MessageConstants.VIEW_SETTINGS_FILTER_TITLE1), FuDocMessageBundle.message(MessageConstants.VIEW_SETTINGS_FILTER_TITLE2)));


    public FuDocGeneralSettingForm(Project project) {
        this.project = project;
    }


    /**
     * 当前页面被初始化之前就会被自动调用的方法
     * 在idea表单设置页面对label1勾选 Custom create
     */
    private void createUIComponents() {
        List<FilterFieldBO> filterFieldList = getFilterFromConfig();
        DefaultTableModel defaultTableModel = new DefaultTableModel(convertTableData(filterFieldList), CUSTOM_TITLE);
        customTable = new JBTable(defaultTableModel);
        customTable.setSelectionMode(ListSelectionModel.MULTIPLE_INTERVAL_SELECTION);
        ToolbarDecorator toolbarDecorator = ToolbarDecorator.createDecorator(customTable);
        //添加一行
        toolbarDecorator.setAddAction(button -> {
            //添加一行
            defaultTableModel.addRow(new Vector[]{});
            filterFieldList.add(new FilterFieldBO());
        });
        //移除一行
        toolbarDecorator.setRemoveAction(anActionButton -> {
            //移除一行
            int[] selectedRows = customTable.getSelectedRows();
            if (Objects.nonNull(selectedRows) && selectedRows.length > 0) {
                for (int selectedRow : selectedRows) {
                    if (selectedRow >= 0) {
                        defaultTableModel.removeRow(selectedRow);
                        filterFieldList.remove(selectedRow);
                    }
                }
            }
        });
        FuTableHelper.addChangeListener(customTable, (row, column, beforeValue, afterValue) -> {
            //当单元格的属性被改变后 会调用该方法
            //对过滤数据扩容
            FilterFieldBO filterFieldBO = resize(filterFieldList, row);
            //将table中编辑后的数据设置到持久化数据对象中
            setValue(filterFieldBO, column, afterValue);
        });
        panel1 = toolbarDecorator.createPanel();
    }


    /**
     * 从配置中获取需要过滤的属性
     */
    private List<FilterFieldBO> getFilterFromConfig() {
        FuDocSetting fuDocSetting = FuDocSetting.getInstance(this.project);
        List<FilterFieldBO> filterFieldBOList = fuDocSetting.getFilterFieldBOList();
        if(Objects.isNull(filterFieldBOList)){
            filterFieldBOList = Lists.newArrayList(DefaultConfig.DEFAULT_FILTER_FIELD_LIST);
            fuDocSetting.setFilterFieldBOList(filterFieldBOList);
        }
//        SettingData settingData = fuDocSetting.getSettingData();
//        CustomerSettingData customerSettingData = settingData.getCustomerSettingData();
//        if (Objects.isNull(customerSettingData)) {
//            customerSettingData = new CustomerSettingData();
//        }
//        List<FilterFieldBO> settings_filter_field = customerSettingData.getSettings_filter_field();
//        if (Objects.isNull(settings_filter_field)) {
//            settings_filter_field = Lists.newArrayList();
//            customerSettingData.setSettings_filter_field(settings_filter_field);
//        }
        return filterFieldBOList;
    }


    /**
     * 点击设置页面应用或则完成时会调用该方法
     */
    public void apply() {
        FuDocSetting fuDocSetting = FuDocSetting.getInstance(this.project);
        SettingData settingData = fuDocSetting.getSettingData();
        int rowCount = this.customTable.getRowCount();
        int columnCount = this.customTable.getColumnCount();
        List<FilterFieldBO> filterFieldBOList = Lists.newArrayList();
        for (int i = 0; i < rowCount; i++) {
            FilterFieldBO filterFieldBO = new FilterFieldBO();
            for (int j = 0; j < columnCount; j++) {
                Object valueAt = customTable.getValueAt(i, j);
                setValue(filterFieldBO, j, Objects.isNull(valueAt) ? StringUtils.EMPTY : valueAt.toString());
            }
            filterFieldBOList.add(filterFieldBO);
        }
        fuDocSetting.setFilterFieldBOList(filterFieldBOList);
        CustomerSettingData customerSettingData = settingData.getCustomerSettingData();
        if (Objects.isNull(customerSettingData)) {
            customerSettingData = new CustomerSettingData();
        }
        customerSettingData.setSettings_filter_field(filterFieldBOList);
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
