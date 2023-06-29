package com.wdf.fudoc.request.view.toolwindow;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiElement;
import com.wdf.fudoc.common.datakey.FuDocDataKey;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.components.listener.SendHttpListener;
import com.wdf.fudoc.components.message.MessageComponent;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.callback.FuRequestCallback;
import com.wdf.fudoc.request.execute.HttpApiExecutor;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.tab.request.ResponseHeaderTabView;
import com.wdf.fudoc.request.tab.request.ResponseTabView;
import com.wdf.fudoc.request.view.FuRequestStatusInfoView;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangdingfu
 * @date 2022-08-25 22:07:47
 */
@Slf4j
public class FuRequestWindow extends SimpleToolWindowPanel implements DataProvider, HttpCallback, SendHttpListener, FuRequestCallback {

    /**
     * 根面板
     */
    @Getter
    private final JPanel rootPanel;

    @Getter
    private final ToolWindow toolWindow;

    @Getter
    private final Project project;

    /**
     * 请求面板
     */
    @Getter
    private final RequestTabView requestTabView;

    /**
     * 响应面板
     */
    private final ResponseTabView responseTabView;

    /**
     * 响应头面板
     */
    private final ResponseHeaderTabView responseHeaderTabView;

    /**
     * 状态信息面板
     */
    private final MessageComponent messageComponent;
    private CompletableFuture<Void> sendHttpTask;

    @Setter
    @Getter
    private PsiElement psiElement;


    private FuHttpRequestData httpRequestData;

    private final AtomicBoolean sendStatus = new AtomicBoolean(false);

    public boolean getSendStatus() {
        return sendStatus.get();
    }

    public FuRequestWindow(@NotNull Project project, ToolWindow toolWindow) {
        super(Boolean.TRUE, Boolean.TRUE);
        this.project = project;
        this.toolWindow = toolWindow;
        this.rootPanel = new JPanel(new BorderLayout());
        Splitter splitter = new Splitter(true, 0.6F);
        FuRequestStatusInfoView fuRequestStatusInfoView = new FuRequestStatusInfoView(project);
        this.requestTabView = new RequestTabView(project, this, fuRequestStatusInfoView);
        this.responseTabView = new ResponseTabView(project, fuRequestStatusInfoView);
        this.responseHeaderTabView = new ResponseHeaderTabView(project);
        splitter.setFirstComponent(this.requestTabView.getRootPane());
        splitter.setSecondComponent(FuTabBuilder.getInstance().addTab(this.responseTabView).addTab(this.responseHeaderTabView).build());
        this.rootPanel.add(splitter, BorderLayout.CENTER);
        this.messageComponent = new MessageComponent(true);
        this.messageComponent.switchInfo();
        this.rootPanel.add(this.messageComponent.getRootPanel(), BorderLayout.SOUTH);
        setContent(this.rootPanel);
    }


    @Override
    public @Nullable Object getData(@NotNull @NonNls String dataId) {
        if (FuDocDataKey.WINDOW_PANE.is(dataId)) {
            return this;
        }
        return super.getData(dataId);
    }

    @Override
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

    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        this.httpRequestData = httpRequestData;
        this.requestTabView.initData(httpRequestData);
        this.responseTabView.initData(httpRequestData);
        this.responseHeaderTabView.initData(httpRequestData);
        this.messageComponent.switchInfo();
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        this.requestTabView.doSendBefore(fuHttpRequestData);
    }

    @Override
    public void doSendAfter(FuHttpRequestData fuHttpRequestData) {
        ApplicationManager.getApplication().invokeLater(() -> {
            //填充响应面板数据
            this.responseTabView.initData(fuHttpRequestData);
            //填充响应头面板数据
            this.responseHeaderTabView.initData(fuHttpRequestData);
        });
    }


    public void initRootPane() {
        this.requestTabView.initRootPane();
        this.responseTabView.initRootPane();
    }


    @Override
    public void doSendHttp() {
        this.sendHttpTask = CompletableFuture.runAsync(() -> {
            sendStatus.set(true);
            doSendBefore(httpRequestData);
            //发起请求
            HttpApiExecutor.doSendRequest(project, httpRequestData);
        });
        //等待请求执行完成
        this.sendHttpTask.join();

        //执行后置逻辑
        sendStatus.set(false);
        doSendAfter(httpRequestData);
        this.sendHttpTask = null;
    }
}
