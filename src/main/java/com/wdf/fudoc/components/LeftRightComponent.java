package com.wdf.fudoc.components;

import com.intellij.openapi.ui.Splitter;
import lombok.Getter;

import javax.swing.*;
import java.awt.*;

/**
 * 左右组件
 *
 * @author wangdingfu
 * @date 2023-02-16 22:16:24
 */
public class LeftRightComponent {

    /**
     * 根面板
     */
    @Getter
    private final JPanel rootPanel;

    /**
     * 左侧面板
     */
    private final JPanel leftPanel;

    /**
     * 右侧面板
     */
    private final JComponent rightPanel;

    public LeftRightComponent(JPanel leftPanel, JComponent rightPanel) {
        this(leftPanel, rightPanel, 0.2F);
    }

    public LeftRightComponent(JPanel leftPanel, JComponent rightPanel, float proportion) {
        this.rootPanel = new JPanel(new BorderLayout());
        this.leftPanel = leftPanel;
        this.rightPanel = rightPanel;
        Splitter splitter = new Splitter(false, proportion);
        splitter.setFirstComponent(this.leftPanel);
        splitter.setSecondComponent(this.rightPanel);
        this.rootPanel.add(splitter, BorderLayout.CENTER);
    }
}
