package com.wdf.fudoc.request.view;

import com.intellij.openapi.project.Project;
import com.intellij.ui.components.JBPanel;
import com.intellij.ui.components.JBPanelWithEmptyText;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorEmptyTextPainter;
import com.wdf.fudoc.components.FuListStringComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.LeftRightComponent;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.pojo.AuthConfigData;
import icons.FuDocIcons;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

import static com.intellij.openapi.wm.impl.IdeBackgroundUtil.getIdeBackgroundColor;

/**
 * 鉴权设置页面
 *
 * @author wangdingfu
 * @date 2023-02-16 21:55:28
 */
public class AuthSettingView implements FuTab, FuActionListener<AuthConfigData> {

    private static final String TITLE = "鉴权配置";

    /**
     * 根面板
     */
    @Getter
    private final JPanel rootPanel;


    private final JComponent rightPanel;

    private final JPanel emptyPanel;


    /**
     * 左侧列表组件 维护多个鉴权
     */
    private final FuListStringComponent<AuthConfigData> leftComponent;

    /**
     * 右侧权限配置页面
     */
    private final AuthConfigView authConfigView;

    private final AtomicBoolean init = new AtomicBoolean(true);


    public AuthSettingView(Project project) {
        this.rootPanel = new JPanel(new BorderLayout());
        this.authConfigView = new AuthConfigView(project);
        this.rightPanel = new JPanel(new BorderLayout());
        this.emptyPanel = createFramePreview();
        this.rightPanel.add(this.emptyPanel, BorderLayout.CENTER);
        this.leftComponent = new FuListStringComponent<>("权限名称", this, AuthConfigData.class);
        this.rootPanel.add(new LeftRightComponent(this.leftComponent.createPanel(), this.rightPanel).getRootPanel(), BorderLayout.CENTER);
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(TITLE, FuDocIcons.FU_AUTH, this.rootPanel).builder();
    }

    @Override
    public void doAction(AuthConfigData data) {
        if (init.get()) {
            init.set(false);
            this.rightPanel.removeAll();
            this.rightPanel.repaint();
            this.rightPanel.add(this.authConfigView.getRootPanel(), BorderLayout.CENTER);
            this.rightPanel.revalidate();
        }
        this.authConfigView.doAction(data);
    }

    @Override
    public void remove(AuthConfigData data) {
        if (this.leftComponent.getSize() == 0) {
            init.set(true);
            this.rightPanel.removeAll();
            this.rightPanel.repaint();
            this.rightPanel.add(this.emptyPanel, BorderLayout.CENTER);
            this.rightPanel.revalidate();
        }
    }

    @Override
    public void doActionAfter(AuthConfigData data) {
        this.authConfigView.doActionAfter(data);
    }


    private static JPanel createFramePreview() {
        FuEditorEmptyTextPainter painter = new FuEditorEmptyTextPainter();
        JBPanelWithEmptyText panel = new JBPanelWithEmptyText() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                painter.paintEmptyText(this, g);
            }

            @Override
            public Color getBackground() {
                return getIdeBackgroundColor();
            }

            @Override
            public boolean isOpaque() {
                return true;
            }
        };
        panel.getEmptyText().clear();
        return panel;
    }

}
