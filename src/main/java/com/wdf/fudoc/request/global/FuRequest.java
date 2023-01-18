package com.wdf.fudoc.request.global;

import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.execute.FuHttpRequest;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2022-09-19 20:15:25
 */
public class FuRequest {

    /**
     * 当前已发起的请求
     */
    private static final Map<Project, FuHttpRequest> REQUEST_MAP = new ConcurrentHashMap<>();

    /**
     * 新增一个请求
     *
     * @param project       当前项目
     * @param fuHttpRequest 请求数据
     */
    public static void addRequest(Project project, FuHttpRequest fuHttpRequest) {
        REQUEST_MAP.put(project, fuHttpRequest);
    }

    /**
     * 获取当前项目中正在发起的请求状态
     *
     * @param project 项目
     * @return 请求状态
     */
    public static boolean getStatus(Project project) {
        FuHttpRequest fuHttpRequest = getRequest(project);
        return Objects.nonNull(fuHttpRequest) && fuHttpRequest.getStatus();
    }

    /**
     * 获取当前项目中正在发起的请求
     *
     * @param project 当前项目
     * @return 正在发起的请求对象
     */
    public static FuHttpRequest getRequest(Project project) {
        return REQUEST_MAP.get(project);
    }


    public static void remove(Project project) {
        REQUEST_MAP.remove(project);
    }
}
