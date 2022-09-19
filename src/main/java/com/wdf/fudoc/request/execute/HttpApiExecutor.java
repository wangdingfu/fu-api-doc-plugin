package com.wdf.fudoc.request.execute;

import cn.hutool.core.thread.ThreadUtil;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.global.FuRequest;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2022-09-19 19:57:13
 */
public class HttpApiExecutor {


    public static void doSendRequest(Project project, FuHttpRequestData fuHttpRequestData, HttpCallback httpCallback) {
        FuHttpRequest fuHttpRequest = new FuHttpRequestImpl(project, fuHttpRequestData);
        //将当前请求添加到全局对象中
        FuRequest.addRequest(project, fuHttpRequest);
        //发起请求
        ProgressManager.getInstance().run(new Task.WithResult<FuHttpRequestData, RuntimeException>(project, "Send", true) {
            @Override
            protected FuHttpRequestData compute(@NotNull ProgressIndicator indicator) throws RuntimeException {
                fuHttpRequest.doSend(httpCallback);
                return fuHttpRequestData;
            }
        });
    }
}
