package com.wdf.fudoc.factory;

import com.google.common.collect.Lists;
import com.wdf.fudoc.constant.enumtype.DynamicDataType;
import com.wdf.fudoc.data.SettingDynamicValueData;
import com.wdf.fudoc.pojo.bo.FilterFieldBO;
import com.wdf.fudoc.view.bo.Column;
import com.wdf.fudoc.view.bo.KeyValueBO;

import javax.swing.table.TableCellEditor;
import java.util.List;

/**
 * 创建FuTable的column的工厂类
 *
 * @author wangdingfu
 * @date 2022-08-17 10:48:42
 */
public class FuTableColumnFactory {

    /**
     * 过滤属性table列
     */
    public static List<Column<FilterFieldBO>> filterColumns() {
        List<Column<FilterFieldBO>> columns = Lists.newArrayList();
        columns.add(new Column<>("类路径", FilterFieldBO::getClassName, FilterFieldBO::setClassName));
        columns.add(new Column<>("字段名(多个用\",\"拼接. 为空则过滤该类所有属性)", FilterFieldBO::getFieldNames, FilterFieldBO::setFieldNames));
        return columns;
    }


    /**
     * 动态自定义数据table列
     */
    public static List<Column<SettingDynamicValueData>> dynamicColumns() {
        //类型列为下拉框编辑器
        TableCellEditor comboBoxEditor = TableCellEditorFactory.createComboBoxEditor(false, DynamicDataType.getCodes());
        List<Column<SettingDynamicValueData>> columns = Lists.newArrayList();
        columns.add(new Column<>("别名", SettingDynamicValueData::getAlias, SettingDynamicValueData::setAlias));
        columns.add(new Column<>("类型", SettingDynamicValueData::getType, SettingDynamicValueData::setType, comboBoxEditor));
        columns.add(new Column<>("值", SettingDynamicValueData::getValue, SettingDynamicValueData::setValue));
        return columns;
    }


    /**
     * 过滤属性table列
     */
    public static List<Column<KeyValueBO>> keyValueColumns() {
//        TableCellEditor booleanEditor = TableCellEditorFactory.createBooleanEditor();
        List<Column<KeyValueBO>> columns = Lists.newArrayList();
//        columns.add(new Column<>("    ", KeyValueBO::getSelect, KeyValueBO::setSelect, booleanEditor));
        columns.add(new Column<>("key", KeyValueBO::getKey, KeyValueBO::setKey));
        columns.add(new Column<>("Value", KeyValueBO::getValue, KeyValueBO::setValue));
        return columns;
    }


}
