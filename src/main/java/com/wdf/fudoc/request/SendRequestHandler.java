package com.wdf.fudoc.request;

import com.intellij.openapi.project.Project;
import com.wdf.fudoc.request.execute.HttpApiExecutor;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import lombok.extern.slf4j.Slf4j;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangdingfu
 * @date 2023-06-29 22:20:49
 */
@Slf4j
public class SendRequestHandler {


    private final Project project;
    private final HttpCallback httpCallback;
    private CompletableFuture<Void> sendHttpTask;
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
        this.sendHttpTask = CompletableFuture.runAsync(() -> {
            sendStatus.set(true);
            httpCallback.doSendBefore(httpRequestData);
            //发起请求
            HttpApiExecutor.doSendRequest(project, httpRequestData);
        });
        //等待请求执行完成
        this.sendHttpTask.join();

        //执行后置逻辑
        sendStatus.set(false);
        httpCallback.doSendAfter(httpRequestData);
        this.sendHttpTask = null;
    }


    public boolean getSendStatus() {
        return sendStatus.get();
    }

    public void stopHttp() {
        if (Objects.isNull(sendHttpTask) || sendHttpTask.isCancelled() || sendHttpTask.isDone()) {
            return;
        }
        try {
            sendHttpTask.cancel(true);
        } catch (Exception e) {
            log.info("终止http请求", e);
        }
    }

}
