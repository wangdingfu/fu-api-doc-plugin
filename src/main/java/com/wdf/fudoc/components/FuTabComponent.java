package com.wdf.fudoc.components;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.wdf.fudoc.apidoc.constant.enumtype.ActionType;
import com.wdf.fudoc.components.listener.TabBarListener;
import com.wdf.fudoc.test.view.bo.BarPanelBO;
import icons.FuDocIcons;
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
    private JComponent defaultPanel;

    private JPanel toolBarPanel;


    private DefaultActionGroup toggleActionGroup;
    private DefaultActionGroup actionGroup;

    /**
     * 点击工具栏按钮后切换的面板
     */
    private Map<String, BarPanelBO> barPanelMap = new ConcurrentHashMap<>();

    /**
     * 工具栏按钮对应的监听器
     */
    private Map<String, TabBarListener> listenerMap = new ConcurrentHashMap<>();


    private String currentTab;


    public FuTabComponent(String title, Icon icon, JComponent mainPanel) {
        this.title = title;
        this.icon = icon;
        this.defaultPanel = mainPanel;
        this.rootPanel = new JPanel(new BorderLayout());
        switchPanel(mainPanel);
    }

    public static FuTabComponent getInstance(String title, Icon icon, JComponent mainPanel) {
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
        addBar(text, icon, targetPanel, ActionType.AN_ACTION);
        return this;
    }

    public FuTabComponent addToggleBar(String text, Icon icon, JPanel targetPanel) {
        addBar(text, icon, targetPanel, ActionType.TOGGLE_ACTION);
        return this;
    }

    public void addBar(String text, Icon icon, JPanel targetPanel, ActionType actionType) {
        barPanelMap.put(text, new BarPanelBO(text, icon, false, targetPanel, actionType));
        addAction(text, icon, actionType);
    }

    public FuTabComponent addBulkEditBar(JPanel bulkEditPanel) {
        this.addBar("Bulk Edit", FuDocIcons.FU_REQUEST_BULK_EDIT, bulkEditPanel);
        return this;
    }

    public FuTabComponent addBulkEditBar(JPanel bulkEditPanel, TabBarListener tabBarListener) {
        this.addBar("Bulk Edit", FuDocIcons.FU_REQUEST_BULK_EDIT, bulkEditPanel);
        listenerMap.put("Bulk Edit", tabBarListener);
        return this;
    }

    /**
     * 设置默认tab
     *
     * @param tab tab名称
     * @return 当前组件
     */
    public FuTabComponent switchTab(String tab) {
        this.currentTab = tab;
        switchPanelByTab(tab);
        return this;
    }

    /**
     * 构建成TabInfo对象
     */
    public TabInfo builder() {
        TabInfo tabInfo = new TabInfo(this.rootPanel);
        if (Objects.nonNull(this.icon)) {
            tabInfo.setIcon(this.icon);
        }
        tabInfo.setText(this.title);
        if (Objects.nonNull(this.toggleActionGroup) || Objects.nonNull(this.actionGroup)) {
            this.toolBarPanel = new BorderLayoutPanel();
            if (Objects.nonNull(this.toggleActionGroup)) {
                genToolBarPanel("fudoc.request.toggle.bar", toggleActionGroup, BorderLayout.WEST);
            }
            if (Objects.nonNull(this.actionGroup)) {
                genToolBarPanel("fudoc.request.toggle.bar", actionGroup, BorderLayout.EAST);
            }
            tabInfo.setSideComponent(toolBarPanel);
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
        if (Objects.isNull(actionGroup)) {
            actionGroup = new DefaultActionGroup();
        }
        actionGroup.add(new AnAction(text, text, icon) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                BarPanelBO barPanelBO = barPanelMap.get(text);
                if (Objects.nonNull(barPanelBO)) {
                    //切换面板
                    barPanelBO.setSelect(!barPanelBO.isSelect());
                    switchPanel(barPanelBO.isSelect() ? barPanelBO.getTargetPanel() : defaultPanel);
                }
                //发出事件通知
                TabBarListener tabBarListener = listenerMap.get(text);
                if (Objects.nonNull(tabBarListener)) {
                    tabBarListener.click(barPanelBO);
                }
            }
        });
    }

    private void addToggleAction(String text, Icon icon) {
        if (Objects.isNull(toggleActionGroup)) {
            toggleActionGroup = new DefaultActionGroup();
        }
        toggleActionGroup.add(new ToggleAction(text, text, icon) {
            @Override
            public boolean isDumbAware() {
                return true;
            }

            @Override
            public boolean isSelected(@NotNull AnActionEvent e) {
                return text.equals(currentTab);
            }

            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                if (!text.equals(currentTab)) {
                    //切换新tab时才切换面板
                    switchPanelByTab(text);
                }
                currentTab = text;
                TabBarListener tabBarListener = listenerMap.get(text);
                if (Objects.nonNull(tabBarListener)) {
                    tabBarListener.select(state);
                }
            }
        });
    }


    private void switchPanelByTab(String tab) {
        //切换新tab时才切换面板
        BarPanelBO barPanelBO = barPanelMap.get(tab);
        switchPanel(barPanelBO.getTargetPanel());
    }

    private void addAction(String text, Icon icon, ActionType actionType) {
        if (ActionType.AN_ACTION.equals(actionType)) {
            addBarAction(text, icon);
        }
        if (ActionType.TOGGLE_ACTION.equals(actionType)) {
            addToggleAction(text, icon);
        }
    }


    public void genToolBarPanel(String place, ActionGroup actionGroup, String layout) {
        ActionToolbarImpl toolbar = (ActionToolbarImpl) ActionManager.getInstance().createActionToolbar(place, actionGroup, true);
        toolbar.setTargetComponent(this.toolBarPanel);
        toolbar.setForceMinimumSize(true);
        toolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
        Utils.setSmallerFontForChildren(toolbar);
        this.toolBarPanel.add(toolbar.getComponent(), layout);
    }


    /**
     * 切换面板
     *
     * @param switchPanel 需要切换的面板
     */
    private void switchPanel(JComponent switchPanel) {
        this.rootPanel.removeAll();
        this.rootPanel.repaint();
        this.rootPanel.add(switchPanel, BorderLayout.CENTER);
        this.rootPanel.revalidate();
    }

}
