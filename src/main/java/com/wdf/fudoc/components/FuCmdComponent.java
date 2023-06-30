package com.wdf.fudoc.components;

import com.intellij.ui.components.ActionLink;
import com.intellij.ui.components.panels.VerticalBox;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.constants.enumtype.ScriptCmd;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Objects;

import static com.intellij.ui.ScrollPaneFactory.createScrollPane;
import static javax.swing.ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;

/**
 * @author wangdingfu
 * @date 2023-06-04 20:48:50
 */
public class FuCmdComponent {

    @Getter
    private final VerticalBox verticalBox;


    private final FuActionListener<ScriptCmd> fuActionListener;


    public FuCmdComponent(FuActionListener<ScriptCmd> listener) {
        this.fuActionListener = listener;
        this.verticalBox = new VerticalBox();
        this.verticalBox.setBorder(JBUI.Borders.empty(10, 0, 16, 16));
    }


    public JScrollPane build() {
        JScrollPane pane = createScrollPane(this.verticalBox, true);
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
        this.verticalBox.add(component);
        this.verticalBox.add(Box.createVerticalStrut(5));
    }

    public void addStrut(int size) {
        this.verticalBox.add(Box.createVerticalStrut(size));
    }

    public void addCmd(String title, List<ScriptCmd> tipCmdList, JComponent component) {
        JLabel titleLabel = new JLabel();
        Font font = titleLabel.getFont();
        titleLabel.setFont(new Font(font.getFontName(), Font.BOLD, 14));
        titleLabel.setText(title);
        this.verticalBox.add(titleLabel);
        this.verticalBox.add(Box.createVerticalStrut(5));
        if (CollectionUtils.isNotEmpty(tipCmdList)) {
            tipCmdList.forEach(this::addCmd);
        }
        if (Objects.nonNull(component)) {
            addComponent(component);
        }
        this.verticalBox.add(Box.createVerticalStrut(20));
    }


    private void addCmd(ScriptCmd scriptCmd) {
        ActionLink actionLink = new ActionLink(scriptCmd.getText(), e -> {
            this.fuActionListener.doAction(scriptCmd);
        });
        actionLink.setForeground(scriptCmd.getColor());
        addAction(actionLink);
    }


    public void addAction(ActionLink actionLink) {
        actionLink.setBorder(JBUI.Borders.empty(1, 14, 3, 1));
        this.verticalBox.add(actionLink);
        this.verticalBox.add(Box.createVerticalStrut(5));
    }

}
