package com.wdf.fudoc.request.view.toolwindow;

import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.SimpleToolWindowPanel;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.common.datakey.FuDocDataKey;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.RequestTabView;
import com.wdf.fudoc.request.tab.ResponseTabView;
import com.wdf.fudoc.test.view.TestRequestFrom;
import lombok.Getter;
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

    /**
     * 请求面板
     */
    @Getter
    private final RequestTabView requestTabView;

    /**
     * 响应面板
     */
    private final ResponseTabView responseTabView;



    public FuRequestWindow(@NotNull Project project, ToolWindow toolWindow) {
        super(Boolean.TRUE, Boolean.TRUE);
        this.toolWindow = toolWindow;
        this.rootPanel = new JPanel(new BorderLayout());
        JSplitPane splitPane = new JSplitPane();
        this.requestTabView = new RequestTabView(project, this);
        this.responseTabView = new ResponseTabView(project);
        splitPane.setOrientation(JSplitPane.VERTICAL_SPLIT);
        splitPane.setTopComponent(this.requestTabView.getRootPane());
        splitPane.setBottomComponent(this.responseTabView.getRootPanel());
        splitPane.setBorder(JBUI.Borders.empty());
        splitPane.setDividerLocation(0.4);
        splitPane.setDividerSize(1);
        splitPane.setBorder(JBUI.Borders.empty());
        splitPane.setContinuousLayout(true);
        this.rootPanel.add(splitPane, BorderLayout.CENTER);
        setContent(this.rootPanel);
    }

    @Override
    protected void paintComponent(Graphics g) {
        this.requestTabView.initRootPane();
        super.paintComponent(g);
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
        this.requestTabView.initRootPane();
        this.responseTabView.initData(httpRequestData);
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        this.requestTabView.doSendBefore(fuHttpRequestData);
    }

    @Override
    public void doSendAfter(FuHttpRequestData fuHttpRequestData) {
        ApplicationManager.getApplication().invokeLater(() -> {
            this.responseTabView.initData(fuHttpRequestData);
        });
    }
}
