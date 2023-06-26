package com.wdf.fudoc.components;

import com.intellij.ui.components.ActionLink;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.listener.FuActionListener;
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

    @Getter
    private final JPanel rootPanel;


    private final FuActionListener<ScriptCmd> fuActionListener;


    public FuCmdComponent(FuActionListener<ScriptCmd> listener) {
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

    public static FuCmdComponent getInstance(FuActionListener<ScriptCmd> listener) {
        return new FuCmdComponent(listener);
    }


    /**
     * 新增展示面板
     *
     * @param component 组件
     */
    public void addComponent(JComponent component) {
        this.rootPanel.add(component);
        this.rootPanel.add(Box.createVerticalStrut(5));
    }

    public void addStrut(int size){
        this.rootPanel.add(Box.createVerticalStrut(size));
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
        addAction(actionLink);
    }


    public void addAction(ActionLink actionLink){
        actionLink.setBorder(JBUI.Borders.empty(1, 14, 3, 1));
        this.rootPanel.add(actionLink);
    }

}
