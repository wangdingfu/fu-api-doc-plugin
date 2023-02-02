package com.wdf.fudoc.request.view;

import com.google.common.collect.Lists;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.wm.impl.status.MemoryUsagePanel;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.widget.HttpCodeWidget;
import com.wdf.fudoc.request.view.widget.HttpContentSizeWidget;
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

    private final JPanel leftPanel;

    private final JPanel centerPanel;

    private JPanel rightPanel;

    private int index = 0;

    private final List<FuWidget> widgetList = Lists.newArrayList();



    public FuRequestStatusInfoView() {
        this.rootPanel = new JPanel(new BorderLayout());
        this.leftPanel = new JPanel();
        this.centerPanel = new JPanel();
        this.rootPanel.add(this.leftPanel, BorderLayout.WEST);
        this.rootPanel.add(this.centerPanel, BorderLayout.CENTER);
        this.rootPanel.setOpaque(true);
        initRightPanel();
        initWidget();
        this.rootPanel.revalidate();
    }


    public void addWidget(FuWidget fuWidget) {
        widgetList.add(fuWidget);
        rightPanel.add(fuWidget.getComponent(),index++);
    }


    private void initWidget() {
        //初始化响应状态码
        addWidget(new HttpCodeWidget());
        //初始化接口请求耗时
        addWidget(new HttpTimeWidget());
        //初始化响应内容大小
        addWidget(new HttpContentSizeWidget());
    }

    public void initData(FuHttpRequestData fuHttpRequestData) {
        for (FuWidget fuWidget : widgetList) {
            fuWidget.initData(fuHttpRequestData);
        }
    }
    private void initRightPanel() {
        if (rightPanel == null) {
            rightPanel = new JPanel();
            rightPanel.setBorder(JBUI.Borders.emptyLeft(1));
            rightPanel.setLayout(new BoxLayout(rightPanel, BoxLayout.X_AXIS) {
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
            rightPanel.setOpaque(false);
            this.rootPanel.add(rightPanel, BorderLayout.EAST);
        }
    }

}
