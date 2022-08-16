package com.wdf.fudoc.view;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.constant.enumtype.DynamicDataType;
import com.wdf.fudoc.data.CustomerSettingData;
import com.wdf.fudoc.data.SettingDynamicValueData;
import com.wdf.fudoc.factory.TableCellEditorFactory;
import com.wdf.fudoc.pojo.bo.FilterFieldBO;
import com.wdf.fudoc.view.bo.Column;
import com.wdf.fudoc.view.components.FuTableComponent;
import lombok.Getter;

import javax.swing.*;
import javax.swing.table.TableCellEditor;
import java.util.List;

/**
 * Fu Doc 基础设置页面
 *
 * @author wangdingfu
 * @date 2022-08-14 21:31:11
 */
public class FuDocGeneralForm {

    /**
     * 根面板(当前配置页面所有的东西都会挂在这个根面板下)
     */
    @Getter
    private JPanel rootPanel;

    /**
     * 过滤属性配置面板
     */
    private JPanel filterPanel;

    /**
     * 自定义数据配置面板
     */
    private JPanel customDataPanel;

    /**
     * 自定义持久化数据
     */
    private final CustomerSettingData customerSettingData;


    public FuDocGeneralForm(Project project, CustomerSettingData customerSettingData) {
        this.customerSettingData = customerSettingData;
    }

    /**
     * 系统会自动调用该方法来完成自定义panel的创建
     */
    private void createUIComponents() {
        //创建过滤属性面板
        this.filterPanel = FuTableComponent.create(createFilterColumn(), customerSettingData.getSettings_filter_field(), FilterFieldBO.class);
        //创建自定义数据面板
        this.customDataPanel = FuTableComponent.create(createCustomColumn(), customerSettingData.getSetting_customer_value(), SettingDynamicValueData.class);
//        this.filterPanel = FuTableComponent.createComponent(FuTableConstants.GENERAL_FILTER_FIELD, customerSettingData.getSettings_filter_field(), FilterFieldBO.class);
//        this.customDataPanel = FuTableComponent.createComponent(FuTableConstants.GENERAL_CUSTOM_DATA, customerSettingData.getSetting_customer_value(), SettingDynamicValueData.class);
    }


    private List<Column<FilterFieldBO>> createFilterColumn() {
        return Lists.newArrayList(
                new Column<>("类路径", FilterFieldBO::getClassName, FilterFieldBO::setClassName),
                new Column<>("字段名(多个用\",\"拼接. 为空则过滤该类所有属性)", FilterFieldBO::getFieldNames, FilterFieldBO::setFieldNames)
        );
    }

    private List<Column<SettingDynamicValueData>> createCustomColumn() {
        TableCellEditor comboBoxEditor = TableCellEditorFactory.createComboBoxEditor(false, DynamicDataType.getCodes());
        return Lists.newArrayList(
                new Column<>("别名", SettingDynamicValueData::getAlias, SettingDynamicValueData::setAlias),
                new Column<>("类型", SettingDynamicValueData::getType, SettingDynamicValueData::setType, comboBoxEditor),
                new Column<>("值", SettingDynamicValueData::getValue, SettingDynamicValueData::setValue)
        );
    }


    /**
     * 当在配置页面点击apply或者OK时 会调用该方法 将页面编辑的内容持久化到文件中
     */
    public void apply() {
    }

    /**
     * 进入该设置页面时会调用该方法
     */
    public void reset() {
    }

}
