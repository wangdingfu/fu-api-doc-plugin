package com.wdf.fudoc.components;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.wm.impl.status.MemoryUsagePanel;
import com.intellij.ui.AnimatedIcon;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.components.listener.FuMsgListener;
import com.wdf.fudoc.request.constants.enumtype.MessageType;
import com.wdf.fudoc.request.msg.FuMsgHandler;
import com.wdf.fudoc.request.msg.FuMsgManager;
import com.wdf.fudoc.request.msg.handler.FuMsgExecutor;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Objects;

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

    private final JLabel myRefreshIcon = new JLabel(new AnimatedIcon.Default());


    public MessageComponent() {
        this.rootPanel = new JPanel(new BorderLayout());
        this.rootPanel.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 6));
        initFuMsgComponent();
        initLeftPanel();
        initCenterPanel();
        initRightPanel();
        switchInfo();
    }


    public void switchInfo() {
        //随机展示一条消息
        fuMsgComponent.setMsg(FuMsgManager.nextMsg());
    }

    private void initFuMsgComponent() {
        this.fuMsgComponent = new FuMsgComponent();
        fuMsgComponent.setForeground(UIUtil.getLabelFontColor(UIUtil.FontColor.BRIGHTER));
        UIUtil.applyStyle(UIUtil.ComponentStyle.SMALL, fuMsgComponent);
        fuMsgComponent.addMsgListener((msgId, fuMsgItemBO) -> {
            MessageType messageType;
            if (Objects.nonNull(fuMsgItemBO) && Objects.nonNull(messageType = MessageType.getEnum(fuMsgItemBO.getMsgType()))) {
                //调用业务逻辑
                FuMsgExecutor fuMsgExecutor = FuMsgHandler.get(messageType);
                if (Objects.nonNull(fuMsgExecutor)) {
                    fuMsgExecutor.execute(msgId, fuMsgItemBO);
                }
            }
        });
    }


    private void initLeftPanel() {
        if (leftPanel == null) {
            leftPanel = new JPanel();
            leftPanel.setBorder(JBUI.Borders.empty(0, 4, 0, 1));
            leftPanel.setLayout(new BoxLayout(leftPanel, BoxLayout.X_AXIS));
            leftPanel.setOpaque(false);
            myRefreshIcon.setVisible(true);
            myRefreshIcon.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //点击之后 切换图标
                    System.out.println("123");
                    switchInfo();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //1s中后在切换图标
                    System.out.println("456");
                }

                @Override
                public void mouseEntered(MouseEvent e) {

                }

                @Override
                public void mouseExited(MouseEvent e) {

                }
            });
            leftPanel.add(myRefreshIcon);
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
