package com.wdf.fudoc.components;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.ui.components.ActionLink;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.constants.enumtype.ScriptCmd;
import com.wdf.fudoc.util.ResourceUtils;
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

    @Getter
    private final JPanel rootPanel;

    private final FuEditorComponent fuEditorComponent;

    private final FuActionListener<ScriptCmd> fuActionListener;


    public FuCmdComponent(FuEditorComponent fuEditorComponent,FuActionListener<ScriptCmd> listener) {
        this.fuEditorComponent = fuEditorComponent;
        this.fuActionListener = listener;
        this.rootPanel = new JPanel();
        this.rootPanel.setBorder(JBUI.Borders.empty(10, 0, 16, 16));
        this.rootPanel.setLayout(new BoxLayout(this.rootPanel, BoxLayout.Y_AXIS));
    }


    public JScrollPane build() {
        JScrollPane pane = createScrollPane(this.rootPanel, true);
        pane.setHorizontalScrollBarPolicy(HORIZONTAL_SCROLLBAR_NEVER);
        return pane;
    }

    public static FuCmdComponent getInstance(FuEditorComponent fuEditorComponent,FuActionListener<ScriptCmd> listener) {
        return new FuCmdComponent(fuEditorComponent,listener);
    }

    public void addCmd(String title, List<ScriptCmd> tipCmdList) {
        JLabel titleLabel = new JLabel();
        Font font = titleLabel.getFont();
        titleLabel.setFont(new Font(font.getFontName(), Font.BOLD, 14));
        titleLabel.setText(title);
        this.rootPanel.add(titleLabel);
        this.rootPanel.add(Box.createVerticalStrut(5));
        if (CollectionUtils.isNotEmpty(tipCmdList)) {
            tipCmdList.forEach(this::addCmd);
        }
        this.rootPanel.add(Box.createVerticalStrut(20));
    }


    private void addCmd(ScriptCmd scriptCmd) {
        ActionLink actionLink = new ActionLink(scriptCmd.getText(), e -> {
            this.fuActionListener.doAction(scriptCmd);
        });
        actionLink.setForeground(scriptCmd.getColor());
        actionLink.setBorder(JBUI.Borders.empty(1, 14, 3, 1));
        this.rootPanel.add(actionLink);
    }


    public void onClick(ScriptCmd scriptCmd) {
        String cmd = scriptCmd.getCmd();
        String content = ResourceUtils.readResource("template/auth/" + cmd);
        if (scriptCmd.isReset()) {
            fuEditorComponent.setContent(content);
        } else {
            fuEditorComponent.append(content);
        }

    }
}
