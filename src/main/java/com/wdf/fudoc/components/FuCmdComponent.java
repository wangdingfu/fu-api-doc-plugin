package com.wdf.fudoc.components;

import com.google.common.collect.Lists;
import com.intellij.openapi.options.Configurable;
import com.intellij.ui.JBColor;
import com.intellij.ui.components.ActionLink;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.bo.TipCmd;
import com.wdf.fudoc.request.constants.enumtype.ScriptCmd;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

import static com.intellij.ui.ScrollPaneFactory.createScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

/**
 * @author wangdingfu
 * @date 2023-06-04 20:48:50
 */
public class FuCmdComponent {

    private final JPanel rootPanel;


    public FuCmdComponent() {
        this.rootPanel = new JPanel();
        this.rootPanel.setBorder(JBUI.Borders.empty(0, 0, 16, 16));
        this.rootPanel.setLayout(new BoxLayout(this.rootPanel, BoxLayout.Y_AXIS));
    }


    public JScrollPane build(){
        JScrollPane pane = createScrollPane(this.rootPanel, true);
        pane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        return pane;
    }

    public static FuCmdComponent getInstance(){
        return new FuCmdComponent();
    }


    public void addCmd(String title, List<ScriptCmd> tipCmdList) {
        JPanel itemPanel = new JPanel(new BorderLayout());
        itemPanel.add(BorderLayout.NORTH, new JLabel(title));
        JPanel cmdPanel = new JPanel();
        cmdPanel.setLayout(new BoxLayout(cmdPanel, BoxLayout.Y_AXIS));
        cmdPanel.setBorder(JBUI.Borders.empty());
        if (CollectionUtils.isNotEmpty(tipCmdList)) {
            tipCmdList.forEach(f -> addCmd(cmdPanel, f));
        }
        itemPanel.add(cmdPanel, BorderLayout.CENTER);
        itemPanel.setBorder(JBUI.Borders.empty(20, 0, 3, 0));
        this.rootPanel.add(itemPanel);
    }

    private void addCmd(JPanel cmdPanel, ScriptCmd scriptCmd) {
        ActionLink actionLink = new ActionLink(scriptCmd.getText(), e -> {
            onClick(scriptCmd);
        });
        actionLink.setForeground(scriptCmd.getColor());
        actionLink.setBorder(JBUI.Borders.empty(1, 10, 3, 1));
        cmdPanel.add(actionLink);
    }


    public void onClick(ScriptCmd scriptCmd) {
        System.out.println("点击了【" + scriptCmd.getText() + "】命令为：" + scriptCmd.getCmd());
    }
}
