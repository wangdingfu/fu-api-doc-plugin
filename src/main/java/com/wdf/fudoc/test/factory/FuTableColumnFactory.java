package com.wdf.fudoc.test.factory;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.DynamicDataType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.apidoc.data.SettingDynamicValueData;
import com.wdf.fudoc.apidoc.pojo.bo.FilterFieldBO;
import com.wdf.fudoc.components.FuTableView;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.test.view.bo.*;

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
    public static List<Column> filterColumns() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new StringColumn<>("类路径", FilterFieldBO::getClassName, FilterFieldBO::setClassName));
        columns.add(new StringColumn<>("字段名(多个用\",\"拼接. 为空则过滤该类所有属性)", FilterFieldBO::getFieldNames, FilterFieldBO::setFieldNames));
        return columns;
    }


    /**
     * 动态自定义数据table列
     */
    public static List<Column> dynamicColumns() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new StringColumn<>("别名", SettingDynamicValueData::getAlias, SettingDynamicValueData::setAlias));
        //类型列为下拉框编辑器
        columns.add(new ComboBoxColumn<>("类型", SettingDynamicValueData::getType, SettingDynamicValueData::setType, DynamicDataType.getCodes()));
        columns.add(new StringColumn<>("值", SettingDynamicValueData::getValue, SettingDynamicValueData::setValue));
        return columns;
    }


    /**
     * 过滤属性table列
     */
    public static List<Column> keyValueColumns() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", KeyValueTableBO::getSelect, KeyValueTableBO::setSelect));
        columns.add(new StringColumn<>("KEY", KeyValueTableBO::getKey, KeyValueTableBO::setKey));
        columns.add(new StringColumn<>("VALUE", KeyValueTableBO::getValue, KeyValueTableBO::setValue));
        columns.add(new StringColumn<>("DESCRIPTION", KeyValueTableBO::getDescription, KeyValueTableBO::setDescription));
        return columns;
    }


    /**
     * 过滤属性table列
     */
    public static List<Column> formDataColumns() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", KeyValueTableBO::getSelect, KeyValueTableBO::setSelect));
        columns.add(new StringColumn<>("KEY", KeyValueTableBO::getKey, KeyValueTableBO::setKey));
        columns.add(new ComboBoxColumn<>("TYPE", KeyValueTableBO::getRequestParamType, KeyValueTableBO::setRequestParamType, RequestParamType.getCodes()));
        columns.add(new StringColumn<>("VALUE", KeyValueTableBO::getValue, KeyValueTableBO::setValue));
        columns.add(new StringColumn<>("DESCRIPTION", KeyValueTableBO::getDescription, KeyValueTableBO::setDescription));
        return columns;
    }



    @SuppressWarnings("all")
    public static <T, R> R getValue(T data, Column column) {
        if (column instanceof StringColumn) {
            return (R) ((StringColumn) column).getGetFun().apply(data);
        }
        if (column instanceof BooleanColumn) {
            return (R) ((BooleanColumn) column).getGetFun().apply(data);
        }
        if (column instanceof ComboBoxColumn) {
            return (R) ((ComboBoxColumn) column).getGetFun().apply(data);
        }
        return null;
    }

    @SuppressWarnings("all")
    public static <T, R> void setValue(T data, R value, Column column) {
        if (column instanceof StringColumn) {
            ((StringColumn) column).getSetFun().accept(data, (String) value);
        }
        if (column instanceof BooleanColumn) {
            ((BooleanColumn) column).getSetFun().accept(data, (Boolean) value);
        }
        if (column instanceof ComboBoxColumn) {
            ((ComboBoxColumn) column).getSetFun().accept(data, (String) value);
        }
    }
}
