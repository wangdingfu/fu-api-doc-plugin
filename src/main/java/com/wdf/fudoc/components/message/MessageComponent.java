package com.wdf.fudoc.components.message;

import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.wm.impl.status.MemoryUsagePanel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.wdf.api.msg.bo.FuMsgBO;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.api.enumtype.MessageType;
import com.wdf.fudoc.components.message.handler.FuMsgExecutor;
import icons.FuDocIcons;
import lombok.Getter;
import lombok.Setter;

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

    private FuMessageComponent fuMessageComponent;

    private final JLabel myRefreshIcon;


    public MessageComponent(boolean isShowIcon) {
        this.rootPanel = new JPanel(new BorderLayout());
        this.rootPanel.setBorder(BorderFactory.createEmptyBorder(1, 0, 0, 6));
        this.myRefreshIcon = isShowIcon ? new JLabel(FuDocIcons.FU_MESSAGE) : new JLabel(" ");
        initFuMsgComponent();
        initLeftPanel();
        initCenterPanel();
        initRightPanel();
    }


    public void switchInfo() {
        //随机展示一条消息
        fuMessageComponent.switchMsg();
    }

    public void setMsg(FuMsgBO fuMsgBO) {
        fuMessageComponent.setMsg(fuMsgBO);
    }

    private void initFuMsgComponent() {
        fuMessageComponent = new FuMessageComponent();
        fuMessageComponent.setForeground(UIUtil.getLabelFontColor(UIUtil.FontColor.BRIGHTER));
        UIUtil.applyStyle(UIUtil.ComponentStyle.SMALL, fuMessageComponent);
        fuMessageComponent.addMsgListener((msgId, fuMsgItemBO) -> {
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
            myRefreshIcon.addMouseListener(new MouseListener() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    //点击之后 切换图标
                    switchInfo();
                }

                @Override
                public void mousePressed(MouseEvent e) {

                }

                @Override
                public void mouseReleased(MouseEvent e) {
                    //1s中后在切换图标
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
            centerPanel.add(this.fuMessageComponent);
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



    public void addWidget(FuWidget fuWidget) {
        this.rightPanel.add(fuWidget.getComponent());
    }
}
