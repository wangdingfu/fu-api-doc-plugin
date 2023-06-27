package com.wdf.fudoc.request.view;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.wm.impl.status.MemoryUsagePanel;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.widget.HttpCodeWidget;
import com.wdf.fudoc.request.view.widget.HttpContentSizeWidget;
import com.wdf.fudoc.request.view.widget.HttpCookieWidget;
import com.wdf.fudoc.request.view.widget.HttpTimeWidget;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * 请求信息面板(响应状态码、耗时、响应内容大小、鉴权用户下拉框等)
 *
 * @author wangdingfu
 * @date 2023-02-01 10:20:14
 */
public class FuRequestStatusInfoView {

    @Getter
    private final JPanel rootPanel;

    private JPanel leftPanel;

    private final JPanel centerPanel;

    private JPanel rightPanel;

    private int leftIndex = 0;
    private int rightIndex = 0;

    private final List<FuWidget> widgetList = Lists.newArrayList();


    public FuRequestStatusInfoView(Project project) {
        this.rootPanel = new JPanel(new BorderLayout());
        this.centerPanel = new JPanel();
        this.rootPanel.add(this.centerPanel, BorderLayout.CENTER);
        this.rootPanel.setOpaque(true);
        initLeftPanel();
        initRightPanel();
        initWidget(project);
    }

    public static FuRequestStatusInfoView getInstance(Project project){
        return new FuRequestStatusInfoView(project);
    }

    public FuRequestStatusInfoView revalidate(){
        this.rootPanel.revalidate();
        return this;
    }


    public FuRequestStatusInfoView addWidget(FuWidget fuWidget) {
        widgetList.add(fuWidget);
        if (fuWidget.isRight()) {
            rightPanel.add(fuWidget.getComponent(), rightIndex++);
        } else {
            this.leftPanel.add(fuWidget.getComponent(), leftIndex++);
        }
        return this;
    }


    private void initWidget(Project project) {
        //初始化响应状态码
        addWidget(new HttpCodeWidget());
        //初始化接口请求耗时
        addWidget(new HttpTimeWidget());
        //初始化cookie面板
        addWidget(new HttpCookieWidget(project));
    }

    public void initData(FuHttpRequestData fuHttpRequestData) {
        for (FuWidget fuWidget : widgetList) {
            fuWidget.initData(fuHttpRequestData);
        }
    }

    private void initRightPanel() {
        if (this.rightPanel == null) {
            this.rightPanel = new JPanel();
            this.rightPanel.setBorder(JBUI.Borders.emptyLeft(1));
            this.rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS) {
                @Override
                public void layoutContainer(Container target) {
                    super.layoutContainer(target);
                    for (Component component : target.getComponents()) {
                        if (component instanceof MemoryUsagePanel) {
                            Rectangle r = component.getBounds();
                            r.y = 0;
                            r.width += SystemInfo.isMac ? 4 : 0;
                            r.height = target.getHeight();
                            component.setBounds(r);
                        }
                    }
                }
            });
            this.rightPanel.setOpaque(false);
            this.rootPanel.add(this.rightPanel, BorderLayout.EAST);
        }
    }

    private void initLeftPanel() {
        if (this.leftPanel == null) {
            this.leftPanel = new JPanel();
            this.leftPanel.setBorder(JBUI.Borders.emptyLeft(1));
            this.leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS) {
                @Override
                public void layoutContainer(Container target) {
                    super.layoutContainer(target);
                    for (Component component : target.getComponents()) {
                        if (component instanceof MemoryUsagePanel) {
                            Rectangle r = component.getBounds();
                            r.y = 0;
                            r.width += SystemInfo.isMac ? 4 : 0;
                            r.height = target.getHeight();
                            component.setBounds(r);
                        }
                    }
                }
            });
            this.leftPanel.setOpaque(false);
            this.rootPanel.add(this.leftPanel, BorderLayout.WEST);
        }
    }

}
