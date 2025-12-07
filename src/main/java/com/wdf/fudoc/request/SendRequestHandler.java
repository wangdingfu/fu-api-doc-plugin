package com.wdf.fudoc.request;

import cn.hutool.core.thread.ThreadUtil;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import cn.fudoc.common.listener.FuDocActionListener;
import cn.fudoc.common.enumtype.FuDocAction;
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
    // IDEA 2025.1+ 新增: 防止 doSendAfter 被重复调用
    private final AtomicBoolean afterCalled = new AtomicBoolean(false);
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

        // IDEA 2025.1+ 修复: 先设置状态,再调用 doSendBefore
        sendStatus.set(true);
        afterCalled.set(false);

        // IDEA 2025.1+ 修复: 在 EDT 线程调用 doSendBefore,确保 UI 更新正确
        ApplicationManager.getApplication().invokeLater(() -> {
            httpCallback.doSendBefore(httpRequestData);
        }, ModalityState.any());

        this.sendHttpTask = ThreadUtil.execAsync(() -> {
            try {
                //发起http请求执行
                HttpApiExecutor.doSendRequest(project, httpRequestData, fuLogger);
                log.info("HTTP请求执行完成");
            } catch (Exception e) {
                log.error("发送HTTP请求异常", e);
            } finally {
                log.info("进入 finally 块, sendHttpTask={}, isCancelled={}",
                    this.sendHttpTask,
                    this.sendHttpTask != null ? this.sendHttpTask.isCancelled() : "null");

                // IDEA 2025.1+ 修复: 无论成功失败,总是调用 doSendAfter 恢复 UI 状态
                // 使用 CAS 确保只调用一次
                if (afterCalled.compareAndSet(false, true)) {
                    ApplicationManager.getApplication().invokeLater(() -> {
                        log.info("EDT 线程执行 doSendAfter (from finally)");
                        try {
                            // 只有未被取消的请求才填充响应数据
                            if (Objects.nonNull(this.sendHttpTask) && !this.sendHttpTask.isCancelled()) {
                                log.info("调用 doSendAfter with data");
                                httpCallback.doSendAfter(httpRequestData);
                            } else {
                                // 被取消的请求也要恢复 UI,但不填充数据
                                log.info("调用 doSendAfter with null");
                                httpCallback.doSendAfter(null);
                            }
                        } catch (Exception e) {
                            log.error("doSendAfter 执行异常", e);
                        } finally {
                            //执行后置逻辑
                            log.info("设置 sendStatus=false, sendHttpTask=null");
                            sendStatus.set(false);
                            this.sendHttpTask = null;
                        }
                    }, ModalityState.any());
                } else {
                    log.info("doSendAfter 已被调用,跳过 (from finally)");
                    // 即使 doSendAfter 被跳过,也要清理状态
                    sendStatus.set(false);
                    this.sendHttpTask = null;
                }
            }
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
            // IDEA 2025.1+ 修复: 取消任务
            log.info("用户点击 Stop 按钮,取消 HTTP 请求");
            this.sendHttpTask.cancel(true);

            // IDEA 2025.1+ 修复: 立即在 EDT 线程恢复 UI,不等待 finally 块
            // 因为被取消的任务可能卡在阻塞 I/O 中,finally 块可能不会立即执行
            // 使用 CAS 确保只调用一次
            if (afterCalled.compareAndSet(false, true)) {
                ApplicationManager.getApplication().invokeLater(() -> {
                    log.info("Stop 按钮触发: 立即调用 doSendAfter 恢复 UI");
                    try {
                        httpCallback.doSendAfter(null);
                    } catch (Exception e) {
                        log.error("doSendAfter 执行异常", e);
                    } finally {
                        sendStatus.set(false);
                        this.sendHttpTask = null;
                    }
                }, ModalityState.any());
            } else {
                log.info("doSendAfter 已被调用,跳过 (from stopHttp)");
            }
        } catch (Exception e) {
            log.info("终止http请求", e);
        }
    }

}
