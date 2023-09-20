package com.wdf.fudoc.request;

import cn.hutool.core.thread.ThreadUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.common.FuDocActionListener;
import com.wdf.fudoc.common.enumtype.FuDocAction;
import com.wdf.fudoc.console.FuLogger;
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
    private final FuLogger fuLogger;


    public SendRequestHandler(Project project, HttpCallback httpCallback, FuLogger fuLogger) {
        this.project = project;
        this.httpCallback = httpCallback;
        this.fuLogger = fuLogger;
    }

    /**
     * 发起http请求
     */
    public void doSend(FuHttpRequestData httpRequestData) {
        if (Objects.isNull(httpRequestData)) {
            return;
        }
        //发布动作事件
        project.getMessageBus().syncPublisher(FuDocActionListener.TOPIC).action(FuDocAction.FU_REQUEST.getCode());
        //清空日志
        fuLogger.clear();
        this.sendHttpTask = ThreadUtil.execAsync(() -> {
            sendStatus.set(true);
            httpCallback.doSendBefore(httpRequestData);
            //发起http请求执行
            HttpApiExecutor.doSendRequest(project, httpRequestData, fuLogger);
            if (Objects.isNull(this.sendHttpTask) || this.sendHttpTask.isCancelled()) {
                return;
            }
            ApplicationManager.getApplication().invokeLater(() -> {
                httpCallback.doSendAfter(httpRequestData);
                //执行后置逻辑
                sendStatus.set(false);
                this.sendHttpTask = null;
            }, ModalityState.any());
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
