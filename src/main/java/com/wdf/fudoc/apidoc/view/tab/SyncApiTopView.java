package com.wdf.fudoc.apidoc.view.tab;

import com.google.common.collect.Lists;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.bo.FuMsgBO;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.components.message.MessageComponent;
import com.wdf.fudoc.request.constants.enumtype.MessageType;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * @author wangdingfu
 * @date 2023-01-10 19:28:15
 */
public class SyncApiTopView {

    @Getter
    private final JPanel rootPanel;

    public SyncApiTopView() {
        this.rootPanel = new JPanel(new BorderLayout());
        MessageComponent messageComponent = new MessageComponent(false);
        messageComponent.setMsg(buildMsg());
        JPanel messagePanel = messageComponent.getRootPanel();
        messagePanel.setBorder(JBUI.Borders.emptyBottom(10));
        this.rootPanel.add(messagePanel, BorderLayout.CENTER);
    }


    private FuMsgBO buildMsg() {
        FuMsgBO fuMsgBO = new FuMsgBO();
        fuMsgBO.setMsgId("fudoc.sync.api.tip.noConfig");
        fuMsgBO.setWeight(1d);
        fuMsgBO.setItemList(Lists.newArrayList(buildTipInfo(), buildDocLink()));
        return fuMsgBO;
    }


    private FuMsgItemBO buildTipInfo() {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setText("您还未添加接口文档系统相关配置 暂时还不能同步接口哦 ");
        fuMsgItemBO.setMsgId("fudoc.sync.api.tip.noConfig.title");
        fuMsgItemBO.setMsgType(MessageType.NORMAL.getCode());
        //红色
        fuMsgItemBO.setRegularColor(FuColor.RED.getRegularColor());
        fuMsgItemBO.setDarkColor(FuColor.RED.getDarkColor());
        return fuMsgItemBO;
    }

    private FuMsgItemBO buildDocLink() {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setText("点我快速了解如何添加这些配置吧!");
        fuMsgItemBO.setMsgId("fudoc.sync.api.tip.noConfig.link");
        fuMsgItemBO.setValue(UrlConstants.FU_DOCUMENT_SYNC_URL);
        fuMsgItemBO.setMsgType(MessageType.LINK.getCode());
        fuMsgItemBO.setRegularColor(FuColor.GITHUB.getRegularColor());
        fuMsgItemBO.setDarkColor(FuColor.GITHUB.getDarkColor());
        return fuMsgItemBO;
    }
}
