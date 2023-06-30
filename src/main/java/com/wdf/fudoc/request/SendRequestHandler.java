package com.wdf.fudoc.request;

import cn.hutool.core.thread.ThreadUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.execute.HttpApiExecutor;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.Future;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangdingfu
 * @date 2023-06-29 22:20:49
 */
@Slf4j
public class SendRequestHandler {


    private final Project project;
    private final HttpCallback httpCallback;
    private Future<?> sendHttpTask;
    private final AtomicBoolean sendStatus = new AtomicBoolean(false);


    public SendRequestHandler(Project project, HttpCallback httpCallback) {
        this.project = project;
        this.httpCallback = httpCallback;
    }

    /**
     * 发起http请求
     */
    public void doSend(FuHttpRequestData httpRequestData) {
        if (Objects.isNull(httpRequestData)) {
            return;
        }
        this.sendHttpTask = ThreadUtil.execAsync(() -> {
            sendStatus.set(true);
            httpCallback.doSendBefore(httpRequestData);
            //发起http请求执行
            HttpApiExecutor.doSendRequest(project, httpRequestData);
            ApplicationManager.getApplication().invokeLater(() -> {
                if (Objects.isNull(this.sendHttpTask) || this.sendHttpTask.isCancelled()) {
                    return;
                }
                httpCallback.doSendAfter(httpRequestData);
                //执行后置逻辑
                sendStatus.set(false);
                this.sendHttpTask = null;
            });
        });
    }


    public boolean getSendStatus() {
        return sendStatus.get();
    }

    public void stopHttp() {
        if (Objects.isNull(this.sendHttpTask) || this.sendHttpTask.isCancelled()) {
            return;
        }
        try {
            this.sendHttpTask.cancel(true);
            //执行后置逻辑
            sendStatus.set(false);
        } catch (Exception e) {
            log.info("终止http请求", e);
        }
    }

}
