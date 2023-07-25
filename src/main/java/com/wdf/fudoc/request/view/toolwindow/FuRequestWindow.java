package com.wdf.fudoc.request.view.toolwindow;

import com.intellij.openapi.actionSystem.DataProvider;
import com.intellij.openapi.application.ApplicationManager;
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
import com.wdf.fudoc.request.SendRequestHandler;
import com.wdf.fudoc.request.callback.FuRequestCallback;
import com.wdf.fudoc.request.manager.FuRequestManager;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestConsoleTabView;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.tab.request.ResponseHeaderTabView;
import com.wdf.fudoc.request.tab.request.ResponseTabView;
import com.wdf.fudoc.request.view.widget.EnvWidget;
import com.wdf.fudoc.request.view.widget.UserWidget;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

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

    private final SendRequestHandler sendRequestHandler;

    private final EnvWidget envWidget;
    private final UserWidget userWidget;

    @Setter
    @Getter
    private PsiElement psiElement;


    private FuHttpRequestData httpRequestData;


    public boolean getSendStatus() {
        return sendRequestHandler.getSendStatus();
    }

    public FuRequestWindow(@NotNull Project project, ToolWindow toolWindow) {
        super(Boolean.TRUE, Boolean.TRUE);
        this.project = project;
        this.toolWindow = toolWindow;
        this.rootPanel = new JPanel(new BorderLayout());
        Splitter splitter = new Splitter(true, 0.6F);
        this.requestTabView = new RequestTabView(project, this, null, toolWindow.getDisposable());
        this.responseTabView = new ResponseTabView(project, null, toolWindow.getDisposable());
        this.envWidget = new EnvWidget(this.project, this.requestTabView, this);
        this.userWidget = new UserWidget(project, this);
        RequestConsoleTabView requestConsoleTabView = new RequestConsoleTabView(this.project, null, toolWindow.getDisposable());
        this.responseHeaderTabView = new ResponseHeaderTabView(project);
        splitter.setFirstComponent(this.requestTabView.getRootPane());
        FuTabBuilder fuTabBuilder = FuTabBuilder.getInstance().addTab(this.responseTabView).addTab(this.responseHeaderTabView).addTab(requestConsoleTabView);
        splitter.setSecondComponent(fuTabBuilder.build());
        this.rootPanel.add(splitter, BorderLayout.CENTER);
        this.messageComponent = new MessageComponent(true);
        this.messageComponent.addWidget(this.envWidget);
        this.messageComponent.addWidget(this.userWidget);
        this.messageComponent.switchInfo();
        this.rootPanel.add(this.messageComponent.getRootPanel(), BorderLayout.SOUTH);
        setContent(this.rootPanel);
        this.sendRequestHandler = new SendRequestHandler(project, this, requestConsoleTabView.getFuLogger());
    }


    @Override
    public void refresh() {
        this.envWidget.refresh();
        this.userWidget.refresh();
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
        this.sendRequestHandler.stopHttp();
    }

    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        this.httpRequestData = httpRequestData;
        this.requestTabView.initData(httpRequestData);
        this.responseTabView.initData(httpRequestData);
        this.responseHeaderTabView.initData(httpRequestData);
        this.messageComponent.switchInfo();
        refresh();
    }

    @Override
    public boolean isWindow() {
        return true;
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
        sendRequestHandler.doSend(httpRequestData);
        try {
            //保存当前请求
            FuRequestManager.saveRequest(project, httpRequestData);
            //保存一些配置数据
            FuRequestConfigStorage.get(project).saveData();
        } catch (Exception e) {
            log.info("持久化请求数据异常", e);
        }

    }
}
