package com.wdf.fudoc.request.po;


import com.google.common.collect.Lists;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;
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



}
