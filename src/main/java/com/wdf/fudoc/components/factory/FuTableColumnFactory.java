package com.wdf.fudoc.components.factory;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.DynamicDataType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.apidoc.data.SettingDynamicValueData;
import com.wdf.fudoc.apidoc.pojo.bo.FilterFieldBO;
import com.wdf.fudoc.apidoc.sync.data.SyncApiTableData;
import com.wdf.fudoc.apidoc.sync.data.YApiProjectTableData;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import com.wdf.fudoc.apidoc.sync.renderer.SyncStatusCellRenderer;
import com.wdf.fudoc.components.ButtonTableCellEditor;
import com.wdf.fudoc.components.bo.*;
import com.wdf.fudoc.request.constants.enumtype.HeaderScope;
import com.wdf.fudoc.request.po.FuCookiePO;
import com.wdf.fudoc.request.po.GlobalKeyValuePO;
import com.wdf.fudoc.request.pojo.CommonHeader;

import javax.swing.table.DefaultTableCellRenderer;
import java.util.List;
import java.util.function.Consumer;

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


    /**
     * 过滤属性table列
     */
    public static List<Column> globalConfig(String key, String value) {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", GlobalKeyValuePO::getSelect, GlobalKeyValuePO::setSelect));
        columns.add(new StringColumn<>(key, GlobalKeyValuePO::getKey, GlobalKeyValuePO::setKey));
        columns.add(new StringColumn<>(value, GlobalKeyValuePO::getValue, GlobalKeyValuePO::setValue));
        columns.add(new TreeModuleComboBoxColumn<>("作用范围", GlobalKeyValuePO::getScope, GlobalKeyValuePO::setScope));
        return columns;
    }


    /**
     * 过滤属性table列
     */
    public static List<Column> cookie() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new StringColumn<>("Name", FuCookiePO::getName, FuCookiePO::setName));
        columns.add(new StringColumn<>("Value", FuCookiePO::getValue, FuCookiePO::setValue));
        columns.add(new StringColumn<>("Domain", FuCookiePO::getDomain, FuCookiePO::setDomain));
        columns.add(new StringColumn<>("Path", FuCookiePO::getPath, FuCookiePO::setPath));
        return columns;
    }


    /**
     * YApi table列
     */
    public static List<Column> yapi() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", YApiProjectTableData::getSelect, YApiProjectTableData::setSelect));
        columns.add(new StringColumn<>("项目token", YApiProjectTableData::getProjectToken, YApiProjectTableData::setProjectToken));
        columns.add(new StringColumn<>("项目名称", YApiProjectTableData::getProjectName, YApiProjectTableData::setProjectName));
        columns.add(new TreeModuleComboBoxColumn<>("作用范围", YApiProjectTableData::getScope, YApiProjectTableData::setScope));
        return columns;
    }


    /**
     * api同步table
     */
    public static List<Column> syncApi() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", SyncApiTableData::getSelect, SyncApiTableData::setSelect));
        columns.add(new StringColumn<>("项目名称", SyncApiTableData::getProjectName, SyncApiTableData::setProjectName));
        columns.add(new StringColumn<>("分类名称", SyncApiTableData::getCategoryName, SyncApiTableData::setCategoryName));
        columns.add(new StringColumn<>("接口名称", SyncApiTableData::getApiName, SyncApiTableData::setApiName));
        columns.add(new StringColumn<>("接口url", SyncApiTableData::getApiUrl, SyncApiTableData::setApiUrl));
        columns.add(new StringColumn<>("同步状态", SyncApiTableData::getSyncStatus, SyncApiTableData::setSyncStatus));
        return columns;
    }

    /**
     * api同步结果table
     */
    public static List<Column> syncApiResult(boolean isShowFailMessage) {
        List<Column> columns = Lists.newArrayList();
        columns.add(new StringColumn<>("接口名称", SyncApiResultDTO::getApiName, SyncApiResultDTO::setApiName));
        columns.add(new StringColumn<>("接口地址", SyncApiResultDTO::getApiUrl, SyncApiResultDTO::setApiUrl));
        columns.add(new StringColumn<>("项目名称", SyncApiResultDTO::getProjectName, SyncApiResultDTO::setProjectName));
        columns.add(new StringColumn<>("接口分类名称", SyncApiResultDTO::getCategoryName, SyncApiResultDTO::setCategoryName));
        columns.add(new StringColumn<>("同步状态", new SyncStatusCellRenderer(), SyncApiResultDTO::getSyncStatus, SyncApiResultDTO::setSyncStatus));
        if (isShowFailMessage) {
            columns.add(new StringColumn<>("失败信息", SyncApiResultDTO::getErrorMsg, SyncApiResultDTO::setErrorMsg));
        }
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
        if (column instanceof TreeModuleComboBoxColumn) {
            return (R) ((TreeModuleComboBoxColumn) column).getGetFun().apply(data);
        }
        if (column instanceof ButtonColumn) {
            return (R) ((ButtonColumn) column).getGetFun().apply(data);
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
        if (column instanceof TreeModuleComboBoxColumn) {
            ((TreeModuleComboBoxColumn) column).getSetFun().accept(data, (TreePathBO) value);
        }
        if (column instanceof ButtonColumn) {
            ((ButtonColumn) column).getSetFun().accept(data, (String) value);
        }
    }
}
