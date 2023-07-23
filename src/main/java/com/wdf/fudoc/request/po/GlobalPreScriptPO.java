package com.wdf.fudoc.request.po;

import com.wdf.fudoc.request.constants.enumtype.ScriptType;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

/**
 * 全局前置脚本持久化对象
 *
 * @author wangdingfu
 * @date 2023-06-10 21:46:45
 */
@Getter
@Setter
public class GlobalPreScriptPO {

    /**
     * 脚本内容
     */
    private String script;

    /**
     * springboot应用
     */
    private String application;

    /**
     * 脚本类型
     */
    private ScriptType scriptType;

    /**
     * 接口请求配置
     */
    private Map<String, FuHttpRequestData> fuHttpRequestDataMap;
}
