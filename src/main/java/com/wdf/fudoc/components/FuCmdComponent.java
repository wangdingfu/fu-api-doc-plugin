package com.wdf.fudoc.components;

import com.intellij.openapi.options.Configurable;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.ActionLink;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.bo.TipCmd;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-06-04 20:48:50
 */
public class FuCmdComponent {

    @Getter
    private final JPanel rootPanel;

    private final JPanel cmdPanel;

    private final List<TipCmd> tipCmdList;

    public FuCmdComponent(String title, List<TipCmd> tipCmdList) {
        this.rootPanel = new JPanel(new BorderLayout());
        this.rootPanel.setBorder(JBUI.Borders.empty(11, 16, 16, 16));
        this.cmdPanel = new JPanel();
        this.cmdPanel.setLayout(new BoxLayout(this.cmdPanel, BoxLayout.Y_AXIS));
        this.cmdPanel.add(Box.createVerticalStrut(10));
        this.tipCmdList = tipCmdList;
        this.rootPanel.add(BorderLayout.NORTH, new JLabel(title));
        this.rootPanel.add(this.cmdPanel, BorderLayout.CENTER);
    }

    public void addCmd(TipCmd tipCmd) {
        this.tipCmdList.add(tipCmd);
        ActionLink actionLink = new ActionLink(tipCmd.getText(), e -> {
            onClick(tipCmd);
        });
        actionLink.setForeground(JBColor.PINK);
        actionLink.setBorder(JBUI.Borders.empty(1, 17, 3, 1));
        this.cmdPanel.add(actionLink);
    }


    public void onClick(TipCmd tipCmd) {
        System.out.println("点击了【" + tipCmd.getText() + "】命令为：" + tipCmd.getCmd());
    }
}
