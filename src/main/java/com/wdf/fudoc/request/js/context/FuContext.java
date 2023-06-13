package com.wdf.fudoc.request.js.context;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.constants.enumtype.ResponseType;
import com.wdf.fudoc.request.execute.HttpExecutor;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.po.GlobalPreScriptPO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;


/**
 * @author wangdingfu
 * @date 2023-05-31 10:19:03
 */
@Getter
@Setter
public class FuContext {

    /**
     * 配置对象
     */
    private final FuRequestConfigPO configPO;

    private final Project project;

    private final GlobalPreScriptPO preScriptPO;

    private final List<String> scope;

    public FuContext(Project project, String scriptName) {
        this.project = project;
        this.configPO = FuRequestConfigStorage.getInstance(project).readData();
        this.preScriptPO = this.configPO.getPreScriptMap().get(scriptName);
        this.scope = Objects.isNull(preScriptPO) ? Lists.newArrayList() : this.preScriptPO.getScope();
    }

    public FuContext(Project project, FuRequestConfigPO configPO, GlobalPreScriptPO preScriptPO) {
        this.project = project;
        this.configPO = configPO;
        this.preScriptPO = preScriptPO;
        this.scope = Objects.isNull(preScriptPO) ? Lists.newArrayList() : this.preScriptPO.getScope();
    }

    public String getScript() {
        return this.preScriptPO.getScript();
    }

    /**
     * 发起接口请求 并返回响应结果
     */
    public String doHttpRequest() {
        FuHttpRequestData fuHttpRequestData = preScriptPO.getFuHttpRequestData();
        if (Objects.isNull(fuHttpRequestData)) {
            return StringUtils.EMPTY;
        }
        //发起请求
        HttpExecutor.execute(project, fuHttpRequestData);

        FuResponseData response = fuHttpRequestData.getResponse();
        return response.getContent();
    }

    /**
     * 设置变量
     *
     * @param variableName 变量名
     * @param value        变量值
     */
    public void setVariable(String variableName, Object value) {
        if (StringUtils.isBlank(variableName) || Objects.isNull(value)) {
            return;
        }
        configPO.addVariable(variableName, value.toString(), this.scope);
    }


    /**
     * 获取变量
     *
     * @param variableName 变量名称
     * @return 变量值
     */
    public Object variable(String variableName) {
        if (StringUtils.isNotBlank(variableName)) {
            return configPO.variable(variableName, this.scope);
        }
        return null;
    }


    /**
     * 设置请求头
     *
     * @param headerName 请求头key
     * @param value      请求头值
     */
    public void setHeader(String headerName, Object value) {
        if (StringUtils.isBlank(headerName) || Objects.isNull(value)) {
            return;
        }
        configPO.addHeader(headerName, value.toString(), scope);
    }

    /**
     * 获取请求头
     *
     * @param headerName 请求头key
     * @return 请求头值
     */
    public Object header(String headerName) {
        if (StringUtils.isNotBlank(headerName)) {
            return configPO.header(headerName, this.scope);
        }
        return null;
    }


}
