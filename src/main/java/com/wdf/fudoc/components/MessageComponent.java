package com.wdf.fudoc.components;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.wm.impl.status.MemoryUsagePanel;
import com.intellij.ui.HyperlinkLabel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.common.constant.TipInfoConstants;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * 【Fu Request】窗口底部消息组件
 *
 * @author wangdingfu
 * @date 2022-11-26 22:05:15
 */
public class MessageComponent {

    @Getter
    private final JPanel rootPanel;

    private JPanel leftPanel;
    private JPanel centerPanel;
    private JPanel rightPanel;

    private FuMsgComponent fuMsgComponent;


    public MessageComponent() {
        this.rootPanel = new JPanel(new BorderLayout());
        this.rootPanel.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 6));
        createReaderModeHyperLink();
        initLeftPanel();
        initCenterPanel();
        initRightPanel();
        switchInfo();
    }


    public void switchInfo() {
        //随机展示一条消息
        fuMsgComponent.setLinkText(TipInfoConstants.get() + "<hyperlink>给我点赞</hyperlink>");
    }


    private void createReaderModeHyperLink() {
        this.fuMsgComponent = new FuMsgComponent();
        fuMsgComponent.setLinkText("测试....<hyperlink>给我点赞</hyperlink>");
        fuMsgComponent.setForeground(UIUtil.getLabelFontColor(UIUtil.FontColor.BRIGHTER));
        UIUtil.applyStyle(UIUtil.ComponentStyle.SMALL, fuMsgComponent);
        fuMsgComponent.addHyperlinkListener(e -> {
            String msgId = e.getDescription();
            System.out.println("123:" + msgId);
        });
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
            centerPanel.add(this.fuMsgComponent);
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
