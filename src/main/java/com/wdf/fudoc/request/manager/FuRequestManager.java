package com.wdf.fudoc.request.manager;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.request.global.GlobalRequestData;
import com.wdf.fudoc.request.http.convert.HttpDataConvert;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.state.FuRequestState;
import com.wdf.fudoc.request.view.HttpDialogView;
import com.wdf.fudoc.storage.FuStorageExecutor;
import com.wdf.fudoc.storage.handler.FuRequestStorage;
import com.wdf.api.util.JsonUtil;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2022-10-08 17:19:54
 */
public class FuRequestManager {


    private static final Map<Project, Map<String, HttpDialogView>> dialogViewMap = new ConcurrentHashMap<>();

    public static void add(HttpDialogView httpDialogView) {

        Map<String, HttpDialogView> map = dialogViewMap.get(httpDialogView.getProject());
        if (Objects.isNull(map)) {
            map = new HashMap<>();
            dialogViewMap.put(httpDialogView.getProject(), map);
        }
        map.put(httpDialogView.getHttpId(), httpDialogView);
    }

    public static void remove(Project project, String httpId) {
        Map<String, HttpDialogView> map = dialogViewMap.get(project);
        if (Objects.nonNull(map)) {
            map.remove(httpId);
        }
    }

    public static HttpDialogView closeAll(Project project) {
        Map<String, HttpDialogView> map = dialogViewMap.get(project);
        if (MapUtils.isEmpty(map)) {
            return null;
        }
        Set<String> httpIdSet = map.keySet();
        if (httpIdSet.size() == 1) {
            return map.get(httpIdSet.iterator().next());
        }
        List<String> httpIdList = Lists.newArrayList(httpIdSet);
        //保留最后一个
        HttpDialogView httpDialogView = map.remove(httpIdList.remove(httpIdList.size() - 1));
        //其余弹框删除
        for (String httpId : httpIdList) {
            HttpDialogView remove = map.remove(httpId);
            //保存请求数据
            remove.doSendBefore(remove.getHttpRequestData());
            //关闭弹框
            remove.close(DialogWrapper.CANCEL_EXIT_CODE);
        }
        return httpDialogView;
    }


    public static String getModuleId(Project project, String moduleName) {
        GlobalRequestData data = FuRequestState.getData(project);
        Map<String, String> moduleIdMap = data.getModuleIdMap();
        String moduleId = moduleIdMap.get(moduleName);
        if (StringUtils.isBlank(moduleId)) {
            moduleId = IdUtil.getSnowflakeNextIdStr();
            moduleIdMap.put(moduleName, moduleId);
        }
        return moduleId;
    }

    public static String getMethodId(Project project, String methodName) {
        GlobalRequestData data = FuRequestState.getData(project);
        Map<String, String> methodIdMap = data.getMethodIdMap();
        String methodId = methodIdMap.get(methodName);
        if (StringUtils.isBlank(methodId)) {
            methodId = IdUtil.getSnowflakeNextIdStr();
            methodIdMap.put(methodName, methodId);
        }
        return methodId;
    }


    /**
     * 保存请求记录
     *
     * @param fuHttpRequestData http请求数据对象
     */
    public static void saveRequest(Project project, FuHttpRequestData fuHttpRequestData) {
        String apiKey;

        if (Objects.isNull(fuHttpRequestData) || StringUtils.isBlank(apiKey = fuHttpRequestData.getApiKey())) {
            return;
        }
//        FuStorageExecutor.saveRequest(fuHttpRequestData);
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
