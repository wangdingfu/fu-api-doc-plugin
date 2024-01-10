package com.wdf.fudoc.components.factory;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.DynamicDataType;
import com.wdf.fudoc.apidoc.constant.enumtype.HeaderLevel;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.apidoc.data.SettingDynamicValueData;
import com.wdf.fudoc.apidoc.pojo.bo.FilterFieldBO;
import com.wdf.fudoc.apidoc.sync.data.ApiFoxProjectTableData;
import com.wdf.fudoc.apidoc.sync.data.ShowDocProjectTableData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiTableData;
import com.wdf.fudoc.apidoc.sync.data.YApiProjectTableData;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import com.wdf.fudoc.apidoc.sync.renderer.SyncStatusCellRenderer;
import com.wdf.fudoc.components.bo.DynamicTableBO;
import com.wdf.fudoc.components.bo.HeaderKeyValueBO;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.bo.TreePathBO;
import com.wdf.fudoc.components.column.*;
import com.wdf.fudoc.request.po.FuCookiePO;
import com.wdf.fudoc.request.po.GlobalKeyValuePO;
import com.wdf.fudoc.request.pojo.ConfigAuthTableBO;
import com.wdf.fudoc.request.pojo.ConfigEnvTableBO;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.List;
import java.util.Objects;

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
        columns.add(new ComboBoxColumn<>("类型", null, SettingDynamicValueData::getType, SettingDynamicValueData::setType, DynamicDataType.getCodes()));
        columns.add(new StringColumn<>("值", SettingDynamicValueData::getValue, SettingDynamicValueData::setValue));
        return columns;
    }


    /**
     * 过滤属性table列
     */
    public static List<Column> keyValueColumns() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", KeyValueTableBO::getSelect, KeyValueTableBO::setSelect));
        columns.add(new StringColumn<>("参数名", KeyValueTableBO::getKey, KeyValueTableBO::setKey));
        columns.add(new StringColumn<>("参数值", KeyValueTableBO::getValue, KeyValueTableBO::setValue));
        columns.add(new StringColumn<>("描述信息", KeyValueTableBO::getDescription, KeyValueTableBO::setDescription));
        return columns;
    }

    /**
     * 自定义表头
     */
    public static List<Column> customConfig() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", KeyValueTableBO::getSelect, KeyValueTableBO::setSelect));
        columns.add(new StringColumn<>("字段英文名", KeyValueTableBO::getKey, KeyValueTableBO::setKey));
        columns.add(new StringColumn<>("字段中文名", KeyValueTableBO::getValue, KeyValueTableBO::setValue));
        return columns;
    }


    /**
     * 过滤属性table列
     */
    public static List<Column> pathVariable() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new StringColumn<>("参数名", KeyValueTableBO::getKey, KeyValueTableBO::setKey));
        columns.add(new StringColumn<>("参数值", KeyValueTableBO::getValue, KeyValueTableBO::setValue));
        columns.add(new StringColumn<>("描述信息", KeyValueTableBO::getDescription, KeyValueTableBO::setDescription));
        return columns;
    }


    /**
     * 过滤属性table列
     */
    public static List<Column> responseHeader() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new StringColumn<>("参数名", HeaderKeyValueBO::getKey, HeaderKeyValueBO::setKey));
        columns.add(new StringColumn<>("参数值", HeaderKeyValueBO::getValue, HeaderKeyValueBO::setValue));
        return columns;
    }

    /**
     * 过滤属性table列
     */
    public static List<Column> header() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", HeaderKeyValueBO::getSelect, HeaderKeyValueBO::setSelect));
        columns.add(new StringColumn<>("参数名", HeaderKeyValueBO::getKey, HeaderKeyValueBO::setKey));
        columns.add(new StringColumn<>("参数值", HeaderKeyValueBO::getValue, HeaderKeyValueBO::setValue));
        columns.add(new ComboBoxColumn<>("级别", null, HeaderKeyValueBO::getLevel, HeaderKeyValueBO::setLevel, HeaderLevel.getCodes()));
        return columns;
    }

    /**
     * 过滤属性table列
     */
    public static List<Column> formDataColumns() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", KeyValueTableBO::getSelect, KeyValueTableBO::setSelect));
        columns.add(new StringColumn<>("参数名", KeyValueTableBO::getKey, KeyValueTableBO::setKey));
        columns.add(new ComboBoxColumn<>("类型", null, KeyValueTableBO::getRequestParamType, KeyValueTableBO::setRequestParamType, RequestParamType.getCodes()));
        columns.add(new StringColumn<>("参数值", KeyValueTableBO::getValue, KeyValueTableBO::setValue));
        columns.add(new StringColumn<>("描述信息", KeyValueTableBO::getDescription, KeyValueTableBO::setDescription));
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
        columns.add(new SpringBootColumn<>("作用范围", GlobalKeyValuePO::getApplicationName, GlobalKeyValuePO::setApplicationName));
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
        columns.add(new SpringBootColumn<>("作用范围", YApiProjectTableData::getApplicationName, YApiProjectTableData::setApplicationName));
        return columns;
    }


    /**
     * YApi table列
     */
    public static List<Column> showDoc() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", ShowDocProjectTableData::getSelect, ShowDocProjectTableData::setSelect));
        columns.add(new StringColumn<>("apiKey", ShowDocProjectTableData::getApiKey, ShowDocProjectTableData::setApiKey));
        columns.add(new StringColumn<>("apiToken", ShowDocProjectTableData::getApiToken, ShowDocProjectTableData::setApiToken));
        columns.add(new StringColumn<>("项目名称", ShowDocProjectTableData::getProjectName, ShowDocProjectTableData::setProjectName));
        columns.add(new SpringBootColumn<>("作用范围", ShowDocProjectTableData::getApplicationName, ShowDocProjectTableData::setApplicationName));
        return columns;
    }


    /**
     * YApi table列
     */
    public static List<Column> apiFox() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", ApiFoxProjectTableData::getSelect, ApiFoxProjectTableData::setSelect));
        columns.add(new StringColumn<>("项目id", ApiFoxProjectTableData::getProjectId, ApiFoxProjectTableData::setProjectId));
        columns.add(new StringColumn<>("项目名称", ApiFoxProjectTableData::getProjectName, ApiFoxProjectTableData::setProjectName));
        columns.add(new SpringBootColumn<>("作用范围", ApiFoxProjectTableData::getApplicationName, ApiFoxProjectTableData::setApplicationName));
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


    /**
     * 环境配置数据
     */
    public static List<Column> envConfig() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", ConfigEnvTableBO::getSelect, ConfigEnvTableBO::setSelect));
        columns.add(new StringColumn<>("环境名称", ConfigEnvTableBO::getEnvName, ConfigEnvTableBO::setEnvName));
        columns.add(new StringColumn<>("域名", ConfigEnvTableBO::getDomain, ConfigEnvTableBO::setDomain));
        columns.add(new SpringBootColumn<>("作用范围", ConfigEnvTableBO::getApplication, ConfigEnvTableBO::setApplication));
        return columns;
    }

    /**
     * 鉴权用户信息
     */
    public static List<Column> authConfig() {
        List<Column> columns = Lists.newArrayList();
        columns.add(new BooleanColumn<>("", ConfigAuthTableBO::getSelect, ConfigAuthTableBO::setSelect));
        columns.add(new StringColumn<>("用户名", ConfigAuthTableBO::getUserName, ConfigAuthTableBO::setUserName));
        columns.add(new StringColumn<>("密码", ConfigAuthTableBO::getPassword, ConfigAuthTableBO::setPassword));
        return columns;
    }


    @SuppressWarnings("all")
    public static <T, R> R getValue(T data, Column column) {
        if (column instanceof DynamicColumn dynamicColumn) {
            //自定义字段名称
            String fieldName = dynamicColumn.getFieldName();
            if (Objects.nonNull(data)
                    && data instanceof DynamicTableBO dynamicTableBO
                    && FuStringUtils.isNotBlank(fieldName)) {
                return (R) dynamicTableBO.getValue(fieldName);
            }
            return null;
        }
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
        if (column instanceof DynamicColumn dynamicColumn) {
            //自定义字段名称
            String fieldName = dynamicColumn.getFieldName();
            if (Objects.nonNull(data)
                    && data instanceof DynamicTableBO dynamicTableBO
                    && FuStringUtils.isNotBlank(fieldName)) {
                dynamicTableBO.setValue(fieldName, value);
            }
        }
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
