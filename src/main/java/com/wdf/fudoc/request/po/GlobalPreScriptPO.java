package com.wdf.fudoc.request.po;

import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

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
     * 脚本tab名称
     */
    private String title;

    /**
     * 脚本内容
     */
    private String script;

    /**
     * 作用范围
     */
    private List<String> scope;

    /**
     * 接口请求配置
     */
    private FuHttpRequestData fuHttpRequestData;
}
