package com.wdf.fudoc.request.js.context;

import cn.hutool.json.JSONUtil;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.console.FuLogger;
import com.wdf.fudoc.request.execute.HttpExecutor;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.po.GlobalPreScriptPO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.text.NumberFormat;
import java.util.Map;
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

    private final String scriptName;

    private final String applicationName;

    private final FuLogger fuLogger;


    public FuContext(Project project, FuRequestConfigPO configPO, GlobalPreScriptPO preScriptPO, FuLogger fuLogger) {
        this.project = project;
        this.configPO = configPO;
        this.preScriptPO = preScriptPO;
        this.scriptName = preScriptPO.getScriptType().getView();
        this.applicationName = preScriptPO.getApplication();
        this.fuLogger = fuLogger;
    }

    public String getScript() {
        return this.preScriptPO.getScript();
    }

    /**
     * 发起接口请求 并返回响应结果
     */
    public String doSend(String key) {
        Map<String, FuHttpRequestData> fuHttpRequestDataMap = preScriptPO.getFuHttpRequestDataMap();
        FuHttpRequestData fuHttpRequestData = fuHttpRequestDataMap.get(key);
        if (Objects.isNull(fuHttpRequestData)) {
            return StringUtils.EMPTY;
        }
        String prefix = fuLogger.getPrefix();
        fuLogger.setPrefix(null);
        //发起请求
        HttpExecutor.execute(project, fuHttpRequestData, this.configPO, fuLogger);

        fuLogger.setPrefix(prefix);
        FuResponseData response = fuHttpRequestData.getResponse();
        return response.getContent();
    }

    public String stringify(Object object) {
        if (Objects.isNull(object)) {
            return StringUtils.EMPTY;
        }
        return JSONUtil.toJsonPrettyStr(object);
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
        configPO.addVariable(variableName, value instanceof Double ? formatDouble((double) value) : value.toString(), this.applicationName);
    }


    /**
     * 获取变量
     *
     * @param variableName 变量名称
     * @return 变量值
     */
    public Object variable(String variableName) {
        if (StringUtils.isNotBlank(variableName)) {
            return configPO.variable(variableName, this.applicationName);
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
        configPO.addHeader(headerName, value.toString(), this.applicationName);
    }

    /**
     * 获取请求头
     *
     * @param headerName 请求头key
     * @return 请求头值
     */
    public Object header(String headerName) {
        if (StringUtils.isNotBlank(headerName)) {
            return configPO.header(headerName, this.applicationName);
        }
        return null;
    }


    /**
     * 将double数字转换成正常数字
     */
    private static String formatDouble(double d) {
        NumberFormat nf = NumberFormat.getInstance();
        //设置保留多少位小数
        nf.setMaximumFractionDigits(0);
        // 取消科学计数法
        nf.setGroupingUsed(false);
        //返回结果
        return nf.format(d);
    }

}
