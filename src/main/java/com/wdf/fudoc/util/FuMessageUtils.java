package com.wdf.fudoc.util;

import com.intellij.util.ui.JBUI;
import cn.fudoc.common.msg.bo.FuMsgBO;
import cn.fudoc.common.msg.FuMsgBuilder;
import com.wdf.fudoc.components.message.MessageComponent;
import cn.fudoc.common.enumtype.MessageType;

import javax.swing.*;
import java.awt.*;

/**
 * @author wangdingfu
 * @date 2023-01-31 11:39:44
 */
public class FuMessageUtils {


    public static JPanel createMessage(FuMsgBO fuMsgBO) {
        JPanel rootPanel = new JPanel(new BorderLayout());
        MessageComponent messageComponent = new MessageComponent(false);
        messageComponent.setMsg(fuMsgBO);
        JPanel messagePanel = messageComponent.getRootPanel();
        messagePanel.setBorder(JBUI.Borders.emptyBottom(10));
        rootPanel.add(messagePanel, BorderLayout.CENTER);
        return rootPanel;
    }

}
