package com.wdf.fudoc.request.manager;

import cn.hutool.json.JSONUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.global.GlobalRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.state.FuRequestState;
import com.wdf.fudoc.util.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-10-08 17:19:54
 */
public class FuRequestManager {


    /**
     * 保存请求记录
     *
     * @param fuHttpRequestData http请求数据对象
     */
    public static void saveRequest(Project project, FuHttpRequestData fuHttpRequestData) {
        if (Objects.isNull(fuHttpRequestData)) {
            return;
        }
        String apiKey = fuHttpRequestData.getApiKey();
        GlobalRequestData data = FuRequestState.getData(project);
        Map<String, String> requestDataMap = data.getRequestDataMap();
        requestDataMap.put(apiKey, JsonUtil.toJson(fuHttpRequestData));
        Map<String, List<String>> recentRequestKeyMap = data.getRecentRequestKeyMap();
        String moduleId = fuHttpRequestData.getModuleId();
        List<String> apiKeyList = recentRequestKeyMap.get(moduleId);
        if (Objects.isNull(apiKeyList)) {
            apiKeyList = Lists.newArrayList();
            recentRequestKeyMap.put(moduleId, apiKeyList);
        }
        apiKeyList.remove(apiKey);
        //控制每个module只缓存最近100个请求
        if (apiKeyList.size() > 100) {
            apiKeyList.remove(0);
        }
        apiKeyList.add(apiKey);
    }


    /**
     * 获取最近一次请求记录
     *
     * @param project  当前项目
     * @param moduleId 当前module标识
     * @return 当前module最近一次请求记录
     */
    public static FuHttpRequestData getRecent(Project project, String moduleId) {
        GlobalRequestData data = FuRequestState.getData(project);
        Map<String, List<String>> recentRequestKeyMap = data.getRecentRequestKeyMap();
        List<String> apiKeyList = recentRequestKeyMap.get(moduleId);
        if (CollectionUtils.isNotEmpty(apiKeyList)) {
            return getRequest(project, apiKeyList.get(apiKeyList.size() - 1));
        }
        return null;
    }


    /**
     * 从内存中获取指定接口的数据信息
     *
     * @param project 当前项目对象
     * @param apiKey  接口唯一标识
     * @return 之前请求过则会返回上一次请求的数据  否则返回null
     */
    public static FuHttpRequestData getRequest(Project project, String apiKey) {
        GlobalRequestData data = FuRequestState.getData(project);
        Map<String, String> requestDataMap = data.getRequestDataMap();
        String entries = requestDataMap.get(apiKey);
        if (StringUtils.isBlank(entries)) {
            return null;
        }
        return JsonUtil.toBean(entries, FuHttpRequestData.class);
    }

}
