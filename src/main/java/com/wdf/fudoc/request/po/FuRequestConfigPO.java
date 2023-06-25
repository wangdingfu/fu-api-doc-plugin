package com.wdf.fudoc.request.po;


import com.google.common.collect.Lists;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.bo.TreePathBO;
import com.wdf.fudoc.request.constants.enumtype.ViewMode;
import com.wdf.fudoc.request.tab.settings.GlobalPreScriptTab;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

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


    public List<GlobalPreScriptPO> getPreScriptList(String scope) {
        List<GlobalPreScriptPO> preScriptPOList = Lists.newArrayList();
        preScriptMap.forEach((key, value) -> {
            if (value.getScope().contains(scope)) {
                preScriptPOList.add(value);
            }
        });
        return preScriptPOList;
    }


    public String header(String headerName, List<String> scope) {
        return globalHeaderList.stream().filter(KeyValueTableBO::getSelect).filter(f -> f.getKey().equals(headerName)).filter(f -> contains(scope, f.getScope().getSelectPathList())).map(KeyValueTableBO::getValue).findFirst().orElse(StringUtils.EMPTY);
    }

    public String variable(String variableName, List<String> scope) {
        return globalVariableList.stream().filter(KeyValueTableBO::getSelect).filter(f -> f.getKey().equals(variableName)).filter(f -> contains(scope, f.getScope().getSelectPathList())).map(KeyValueTableBO::getValue).findFirst().orElse(StringUtils.EMPTY);
    }

    public String variable(String variableName) {
        return globalVariableList.stream().filter(KeyValueTableBO::getSelect).filter(f -> f.getKey().equals(variableName)).map(KeyValueTableBO::getValue).findFirst().orElse(StringUtils.EMPTY);
    }



    private boolean contains(List<String> scope1, List<String> scope2) {
        if (CollectionUtils.isEmpty(scope1) || CollectionUtils.isEmpty(scope2)) {
            return false;
        }
        return scope1.stream().anyMatch(scope2::contains);
    }

    public void addHeader(String headerName, String headerValue, List<String> scope) {
        GlobalKeyValuePO globalKeyValuePO = globalHeaderList.stream().filter(f -> f.getKey().equals(headerName)).findFirst().orElse(null);
        if (Objects.isNull(globalKeyValuePO)) {
            globalKeyValuePO = new GlobalKeyValuePO();
            this.globalHeaderList.add(globalKeyValuePO);
        }
        globalKeyValuePO.setScope(new TreePathBO(scope));
        globalKeyValuePO.setKey(headerName);
        globalKeyValuePO.setValue(headerValue);
        globalKeyValuePO.setSelect(true);
        this.globalHeaderList.add(globalKeyValuePO);
    }

    public void addVariable(String variableName, String variableValue, List<String> scope) {
        GlobalKeyValuePO globalKeyValuePO = globalVariableList.stream().filter(f -> f.getKey().equals(variableName)).findFirst().orElse(null);
        if (Objects.isNull(globalKeyValuePO)) {
            globalKeyValuePO = new GlobalKeyValuePO();
            this.globalVariableList.add(globalKeyValuePO);
        }
        globalKeyValuePO.setScope(new TreePathBO(scope));
        globalKeyValuePO.setKey(variableName);
        globalKeyValuePO.setValue(variableValue);
        globalKeyValuePO.setSelect(true);
    }
}
