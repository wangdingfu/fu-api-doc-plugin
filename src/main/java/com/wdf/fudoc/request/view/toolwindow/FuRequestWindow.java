package com.wdf.fudoc.request.view.toolwindow;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.ui.Splitter;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.psi.PsiElement;
import com.wdf.fudoc.common.datakey.FuDocDataKey;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.components.message.MessageComponent;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.tab.request.ResponseHeaderTabView;
import com.wdf.fudoc.request.tab.request.ResponseTabView;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * @author wangdingfu
 * @date 2022-08-25 22:07:47
 */
public class FuRequestWindow extends SimpleToolWindowPanel implements DataProvider, HttpCallback {

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


    @Setter
    @Getter
    private PsiElement psiElement;


    public FuRequestWindow(@NotNull Project project, ToolWindow toolWindow) {
        super(Boolean.TRUE, Boolean.TRUE);
        this.project = project;
        this.toolWindow = toolWindow;
        this.rootPanel = new JPanel(new BorderLayout());
        Splitter splitter = new Splitter(true, 0.6F);
        this.requestTabView = new RequestTabView(project, this);
        this.responseTabView = new ResponseTabView(project);
        this.responseHeaderTabView = new ResponseHeaderTabView();
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
    public void initData(FuHttpRequestData httpRequestData) {
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


}
