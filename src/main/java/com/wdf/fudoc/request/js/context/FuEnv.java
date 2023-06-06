package com.wdf.fudoc.request.js.context;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2023-06-06 20:03:25
 */
@Getter
public class FuEnv {

    /**
     * 变量集合
     */
    private final Map<String, Object> variableMap = new ConcurrentHashMap<>();
    /**
     * 请求头集合
     */
    private final Map<String, Object> headerMap = new ConcurrentHashMap<>();


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
}
