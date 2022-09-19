package com.wdf.fudoc.request.execute;

import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.global.FuRequest;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;

/**
 * @author wangdingfu
 * @date 2022-09-19 19:57:13
 */
public class HttpApiExecutor {


    public static void doSendRequest(Project project, FuHttpRequestData fuHttpRequestData) {
        FuHttpRequest fuHttpRequest = new FuHttpRequestImpl(project, fuHttpRequestData);
        //将当前请求添加到全局对象中
        FuRequest.addRequest(project, fuHttpRequest);
        //发起请求
        fuHttpRequest.doSend();
    }
}
