package com.wdf.fudoc.components;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.wm.impl.status.MemoryUsagePanel;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.common.constant.TipInfoConstants;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * 自定义的状态栏小部件
 *
 * @author wangdingfu
 * @date 2022-09-30 16:02:25
 */
public class FuStatusBarComponent {

    @Getter
    private final JPanel rootPanel;

    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;

    private final InfoAndProgressPanel infoAndProgressPanel;


    public void add(JComponent jComponent) {
        this.leftPanel.add(jComponent);
    }

    public void setInfo(String info) {
        this.infoAndProgressPanel.setText(info);
    }

    public FuStatusBarComponent() {
        this.rootPanel = new JPanel(new BorderLayout());
        this.rootPanel.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 6));
        this.infoAndProgressPanel = new InfoAndProgressPanel();
        initLeftPanel();
        initCenterPanel();
        initRightPanel();
        switchInfo();
    }

    public void switchInfo() {
        //随机展示一条消息
        setInfo(TipInfoConstants.get());
    }


    private void initLeftPanel() {
        if (leftPanel == null) {
            leftPanel = new JPanel();
            leftPanel.setBorder(JBUI.Borders.empty(0, 4, 0, 1));
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
            leftPanel.setOpaque(false);
            this.rootPanel.add(leftPanel, BorderLayout.WEST);
        }
    }

    private void initCenterPanel() {
        if (centerPanel == null) {
            centerPanel = JBUI.Panels.simplePanel().andTransparent();
            centerPanel.setBorder(JBUI.Borders.empty(0, 1));
            centerPanel.add(infoAndProgressPanel);
            this.rootPanel.add(centerPanel, BorderLayout.CENTER);
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
