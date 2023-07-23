package com.wdf.fudoc.request.po;


import com.google.common.collect.Lists;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.bo.TreePathBO;
import com.wdf.fudoc.request.constants.enumtype.ScriptType;
import com.wdf.fudoc.request.constants.enumtype.ViewMode;
import com.wdf.fudoc.request.pojo.ConfigAuthTableBO;
import com.wdf.fudoc.request.pojo.ConfigEnvTableBO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [Fu Request]配置持久化对象
 *
 * @author wangdingfu
 * @date 2023-06-07 23:38:12
 */
@Getter
@Setter
public class FuRequestConfigPO {

    /**
     * 【Fu Request】展示模式
     */
    private String viewMode = ViewMode.SINGLE_PINNED.myActionID;

    /**
     * 是否自动从配置文件读取启动端口
     */
    private boolean autoPort = true;

    /**
     * 全局请求头
     */
    private List<GlobalKeyValuePO> globalHeaderList = Lists.newArrayList();

    /**
     * 全局变量
     */
    private List<GlobalKeyValuePO> globalVariableList = Lists.newArrayList();

    /**
     * 前置脚本集合
     */
    private Map<String, GlobalPreScriptPO> preScriptMap = new ConcurrentHashMap<>();

    /**
     * 后置脚本集合
     */
    private Map<String, GlobalPreScriptPO> postScriptMap = new ConcurrentHashMap<>();

    private List<ConfigEnvTableBO> envConfigList = Lists.newArrayList();

    private List<ConfigAuthTableBO> authConfigList = Lists.newArrayList();

    /**
     * 自定义表格配置
     */
    private Map<String, List<KeyValueTableBO>> customTableConfigMap = new HashMap<>();

    /**
     * cookie集合
     */
    private List<FuCookiePO> cookies = Lists.newArrayList();


    /**
     * 当前选中的用户名
     */
    private String userName;

    /**
     * 当前选中的环境
     */
    private Map<String, String> defaultEnvMap = new HashMap<>();

    public void addDefaultEnv(String moduleName, String defaultEnv) {
        if (StringUtils.isBlank(moduleName) || StringUtils.isBlank(defaultEnv)) {
            return;
        }
        defaultEnvMap.put(moduleName, defaultEnv);
    }

    public String getEnv(String moduleName) {
        if (StringUtils.isBlank(moduleName)) {
            return StringUtils.EMPTY;
        }
        return defaultEnvMap.get(moduleName);
    }


    public Map<String, GlobalPreScriptPO> getScript(ScriptType scriptType) {
        if (ScriptType.PRE_SCRIPT.equals(scriptType)) {
            return this.preScriptMap;
        }
        if (ScriptType.POST_SCRIPT.equals(scriptType)) {
            return this.postScriptMap;
        }
        return null;
    }


    public GlobalPreScriptPO getScript(ScriptType scriptType, String applicationName) {
        Map<String, GlobalPreScriptPO> script = getScript(scriptType);
        if (MapUtils.isNotEmpty(script)) {
            return script.get(applicationName);
        }
        return null;
    }


    public String header(String headerName, String applicationName) {
        return getValue(findGlobalConfig(this.globalHeaderList, headerName, applicationName));

    }

    public String variable(String variableName, String applicationName) {
        return getValue(findGlobalConfig(this.globalVariableList, variableName, applicationName));
    }

    private String getValue(GlobalKeyValuePO globalKeyValuePO) {
        return Objects.isNull(globalKeyValuePO) ? StringUtils.EMPTY : globalKeyValuePO.getValue();
    }


    private GlobalKeyValuePO findGlobalConfig(List<GlobalKeyValuePO> list, String variableName, String applicationName) {
        return list.stream()
                .filter(KeyValueTableBO::getSelect)
                .filter(f -> StringUtils.isNotBlank(f.getApplicationName()))
                .filter(f -> StringUtils.isNotBlank(f.getKey()))
                .filter(f -> f.getKey().equals(variableName))
                .filter(f -> applicationName.equals(f.getApplicationName()))
                .findFirst().orElse(null);
    }


    private void addGlobalConfig(List<GlobalKeyValuePO> list, String applicationName, String configKey, String configValue) {
        GlobalKeyValuePO globalConfig = findGlobalConfig(list, configKey, applicationName);
        if (Objects.isNull(globalConfig)) {
            globalConfig = new GlobalKeyValuePO();
            list.add(globalConfig);
        }
        globalConfig.setKey(configKey);
        globalConfig.setValue(configValue);
        globalConfig.setApplicationName(applicationName);
        globalConfig.setSelect(true);
    }


    public void addHeader(String headerName, String headerValue, String applicationName) {
        addGlobalConfig(this.globalHeaderList, applicationName, headerName, headerValue);
    }

    public void addVariable(String variableName, String variableValue, String applicationName) {
        addGlobalConfig(this.globalVariableList, applicationName, variableName, variableValue);
    }


    public void addCookies(List<FuCookiePO> cookies) {
        if (CollectionUtils.isEmpty(cookies)) {
            return;
        }
        List<String> nameList = cookies.stream().map(FuCookiePO::getName).toList();
        //移除重复的cookie
        this.cookies.removeIf(f -> nameList.contains(f.getName()));
        this.cookies.addAll(cookies);
    }
}
