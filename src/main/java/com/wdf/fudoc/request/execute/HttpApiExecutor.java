package com.wdf.fudoc.request.execute;

import cn.hutool.core.thread.ThreadUtil;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.global.FuRequest;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;

/**
 * @author wangdingfu
 * @date 2022-09-19 19:57:13
 */
public class HttpApiExecutor {


    /**
     * 发起请求
     *
     * @param project           当前项目
     * @param fuHttpRequestData 请求数据
     * @param httpCallback      请求完毕后的回调
     */
    public static void doSendRequest(Project project, FuHttpRequestData fuHttpRequestData, HttpCallback httpCallback) {
        FuHttpRequest fuHttpRequest = new FuHttpRequestImpl(project, fuHttpRequestData);
        //将当前请求添加到全局对象中
        FuRequest.addRequest(project, fuHttpRequest);
        //创建一个线程异步发起请求
        ThreadUtil.execAsync(() -> fuHttpRequest.doSend(httpCallback));

    }
}
