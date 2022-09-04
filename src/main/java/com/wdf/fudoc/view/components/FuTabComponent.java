package com.wdf.fudoc.view.components;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.wdf.fudoc.view.bo.BarPanelBO;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 【Fu Tab】组件 类似JTabbedPane功能 在此功能基础上新增了toolbar工具栏 支持通过点击bar来切换不同的面板
 *
 * @author wangdingfu
 * @date 2022-09-04 20:49:54
 */
@Getter
@Setter
public class FuTabComponent {

    /**
     * tab标题
     */
    private String title;
    /**
     * tab图标
     */
    private Icon icon;

    /**
     * 根面板 最终展示内容的面板
     */
    private JPanel rootPanel;

    /**
     * 当前tab下默认面板
     */
    private JPanel defaultPanel;

    /**
     * tab栏上的工具按钮面板
     */
    private JPanel toolBarPanel;

    /**
     * 点击工具栏按钮后切换的面板
     */
    private Map<String, BarPanelBO> barPanelMap = new ConcurrentHashMap<>();

    private Map<String, Boolean> barSelectMap = new ConcurrentHashMap<>();

    public FuTabComponent(String title, Icon icon, JPanel mainPanel) {
        this.title = title;
        this.icon = icon;
        this.defaultPanel = mainPanel;
        this.rootPanel = new BorderLayoutPanel();
        switchPanel(mainPanel);
    }

    public static FuTabComponent getInstance(String title, Icon icon, JPanel mainPanel) {
        return new FuTabComponent(title, icon, mainPanel);
    }

    /**
     * 为当前tab新增扩展功能 支持通过点击扩展的bar按钮来切换不同面板
     *
     * @param text        新增扩展bar的标题
     * @param icon        扩展bar的图标
     * @param targetPanel 点击扩展bar会被切换的目标面板
     */
    public FuTabComponent addBar(String text, Icon icon, JPanel targetPanel) {
        barPanelMap.put(text, new BarPanelBO(text, icon, false, targetPanel));
        addBarAction(text, icon);
        return this;
    }


    /**
     * 构建成TabInfo对象
     */
    public TabInfo builder() {
        TabInfo tabInfo = new TabInfo(this.rootPanel);
        tabInfo.setIcon(this.icon).setText(this.title);
        if (Objects.nonNull(this.toolBarPanel)) {
            tabInfo.setSideComponent(this.toolBarPanel);
        }
        return tabInfo;
    }

    /**
     * 添加一个bar 点击bar默认动作为切换面板
     *
     * @param text bar显示的文本
     * @param icon bar显示的图标
     */
    private void addBarAction(String text, Icon icon) {
        if (Objects.isNull(this.toolBarPanel)) {
            this.toolBarPanel = new BorderLayoutPanel();
        }
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new AnAction(text, text, icon) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                BarPanelBO barPanelBO = barPanelMap.get(text);
                if (Objects.nonNull(barPanelBO)) {
                    //切换面板
                    barPanelBO.setSelect(!barPanelBO.isSelect());
                    switchPanel(barPanelBO.isSelect() ? barPanelBO.getTargetPanel() : defaultPanel);
                }
            }
        });
        ActionToolbarImpl toolbar = (ActionToolbarImpl) ActionManager.getInstance().createActionToolbar("FuRequestToolBar", actionGroup, true);
        toolbar.setTargetComponent(this.toolBarPanel);
        toolbar.setForceMinimumSize(true);
        toolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
        Utils.setSmallerFontForChildren(toolbar);
        this.toolBarPanel.add(toolbar.getComponent(), BorderLayout.EAST);
    }

    /**
     * 切换面板
     *
     * @param switchPanel 需要切换的面板
     */
    private void switchPanel(JPanel switchPanel) {
        this.rootPanel.removeAll();
        this.rootPanel.repaint();
        this.rootPanel.add(switchPanel, BorderLayout.CENTER);
        this.rootPanel.revalidate();
    }

}
