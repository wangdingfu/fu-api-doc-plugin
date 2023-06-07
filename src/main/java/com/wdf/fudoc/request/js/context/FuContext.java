package com.wdf.fudoc.request.js.context;

import cn.hutool.json.JSON;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;


/**
 * @author wangdingfu
 * @date 2023-05-31 10:19:03
 */
@Getter
@Setter
public class FuContext {


    /**
     * 变量集合
     */
    private final Map<String, Object> variableMap = new ConcurrentHashMap<>();
    /**
     * 请求头集合
     */
    private final Map<String, Object> headerMap = new ConcurrentHashMap<>();


    /**
     * 响应结果(配置的前置请求返回的结果)
     */
    private JSON result;


    /**
     * 发起鉴权接口请求 并返回响应结果
     */
    public JSON doRequest() {
        return null;
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
        variableMap.put(variableName, value);
    }


    /**
     * 获取变量
     *
     * @param variableName 变量名称
     * @return 变量值
     */
    public Object variable(String variableName) {
        if (StringUtils.isNotBlank(variableName)) {
            return variableMap.get(variableName);
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
        headerMap.put(headerName, value);
    }

    /**
     * 获取请求头
     *
     * @param headerName 请求头key
     * @return 请求头值
     */
    public Object header(String headerName) {
        if (StringUtils.isNotBlank(headerName)) {
            return headerMap.get(headerName);
        }
        return null;
    }


}
