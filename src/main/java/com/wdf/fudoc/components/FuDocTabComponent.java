package com.wdf.fudoc.components;

import cn.hutool.core.map.MapUtil;
import com.intellij.openapi.actionSystem.*;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.apidoc.constant.enumtype.ActionType;
import com.wdf.fudoc.components.bo.TabActionBO;
import com.wdf.fudoc.components.listener.TabBarListener;
import com.wdf.fudoc.util.ToolBarUtils;
import icons.FuDocIcons;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * tab编辑器组件
 * 支持tab右侧添加工具栏
 *
 * @author wangdingfu
 * @date 2022-11-06 10:22:26
 */
@Getter
@Setter
public class FuDocTabComponent {
    private static final String DEFAULT = "DEFAULT";
    private static final String BULK_EDIT = "BULK_EDIT";
    private static final String TOOLBAR_LEFT_PLACE = "fudoc.request.toolbar.left";
    private static final String TOOLBAR_RIGHT_PLACE = "fudoc.request.toolbar.right";
    /**
     * 根面板 最终展示内容的面板
     */
    private final JPanel rootPanel = new JPanel(new BorderLayout());

    /**
     * tab标题
     */
    private String title;
    /**
     * tab图标
     */
    private Icon icon;

    /**
     * 当前tab页面展示的组件
     */
    private JComponent mainComponent;

    /**
     * 工具栏动作分组集合
     */
    private Map<String, DefaultActionGroup> actionGroupMap = new ConcurrentHashMap<>();

    /**
     * tab动作参数
     * key:tab的title value:当前tab的参数对象
     */
    private final Map<String, TabActionBO> tabActionMap = new ConcurrentHashMap<>();

    /**
     * 工具栏当前展示的tab
     */
    private final Map<String, String> currentTabMap = new ConcurrentHashMap<>();

    /**
     * 工具栏面板
     */
    private final JPanel toolBarPanel;

    /**
     * 一级工具栏面板
     */
    private final JPanel toolBarLeftPanel;
    /**
     * 二级工具栏面板
     */
    private final JPanel toolBarRightPanel;


    public FuDocTabComponent(String title, Icon icon, JComponent mainComponent) {
        this.title = title;
        this.icon = icon;
        this.mainComponent = mainComponent;
        this.toolBarPanel = new JPanel(new BorderLayout());
        this.toolBarLeftPanel = new JPanel(new BorderLayout());
        this.toolBarRightPanel = new JPanel(new BorderLayout());
        this.toolBarPanel.add(this.toolBarLeftPanel, BorderLayout.WEST);
        this.toolBarPanel.add(this.toolBarRightPanel, BorderLayout.EAST);
        switchTab(title);
    }


    public static FuDocTabComponent getInstance(String title, Icon icon, JComponent mainPanel) {
        return new FuDocTabComponent(title, icon, mainPanel);
    }


    public TabInfo builder() {
        TabInfo tabInfo = new TabInfo(this.rootPanel);
        if (Objects.nonNull(this.icon)) {
            tabInfo.setIcon(this.icon);
        }
        tabInfo.setText(this.title);
        DefaultActionGroup actionGroup;
        if (MapUtil.isNotEmpty(actionGroupMap) && Objects.nonNull(actionGroup = actionGroupMap.get(DEFAULT))) {
            //构建一级工具栏
            ToolBarUtils.addActionToToolBar(this.toolBarLeftPanel, TOOLBAR_LEFT_PLACE, actionGroup, BorderLayout.WEST);
            //构建二级工具栏
            tabActionMap.forEach((key, value) -> {
                DefaultActionGroup defaultActionGroup;
                if (CollectionUtils.isNotEmpty(value.getChildList()) && Objects.nonNull(defaultActionGroup = actionGroupMap.get(key))) {
                    //生成二级工具栏面板
                    value.setChildPanel(ToolBarUtils.genToolBarPanel(TOOLBAR_RIGHT_PLACE, defaultActionGroup, BorderLayout.EAST));
                }
            });
            tabInfo.setSideComponent(toolBarPanel);
        }
        return tabInfo;
    }

    /**
     * 往tab右侧的工具栏中添加一个动作
     *
     * @param title       动作名称
     * @param icon        动作图标
     * @param actionPanel 当前动作触发后切换到指定的面板
     * @return tab组件对象
     */
    public FuDocTabComponent addAction(String title, Icon icon, JPanel actionPanel) {
        return addAction(title, icon, actionPanel, null, null);
    }

    /**
     * 往tab右侧的工具栏中添加批量编辑按钮
     *
     * @param actionPanel 点击批量编辑按钮切换的面板
     * @return tab组件对象
     */
    public FuDocTabComponent addBulkEditBar(JPanel actionPanel) {
        return initAction(DEFAULT, buildBulkEditTab(actionPanel, null));
    }


    /**
     * 往tab右侧的工具栏中添加一个按钮 并且针对该按钮添加一个批量编辑按钮
     *
     * @param title          按钮名称
     * @param icon           按钮图标
     * @param actionPanel    点击按钮切换到指定的面板
     * @param bulkEditPanel  批量编辑面板(将批量编辑面板挂在当前工具栏下 点击当前工具栏会出现对应批量编辑按钮进入批量编辑面板 类似二级菜单效果)
     * @param tabBarListener 批量编辑面板按钮监听器 当点击批量编辑按钮时触发 告诉调用者当前点击了编辑按钮
     * @return tab组件对象
     */
    public FuDocTabComponent addAction(String title, Icon icon, JPanel actionPanel, JPanel bulkEditPanel, TabBarListener tabBarListener) {
        TabActionBO tabActionBO = new TabActionBO(title, icon, actionPanel, ActionType.TOGGLE_ACTION);
        if (Objects.nonNull(bulkEditPanel)) {
            tabActionBO.addChild(buildBulkEditTab(bulkEditPanel, tabBarListener));
        }
        //初始化动作
        return initAction(DEFAULT, tabActionBO);
    }


    public FuDocTabComponent initAction(String parentTab, TabActionBO tabActionBO) {
        DefaultActionGroup defaultActionGroup = actionGroupMap.get(parentTab);
        if (Objects.isNull(defaultActionGroup)) {
            defaultActionGroup = new DefaultActionGroup();
            actionGroupMap.put(parentTab, defaultActionGroup);
        }
        if (DEFAULT.equals(parentTab)) {
            tabActionMap.put(tabActionBO.getTitle(), tabActionBO);
        }
        ActionType actionType = tabActionBO.getActionType();
        defaultActionGroup.add(ActionType.AN_ACTION.equals(actionType) ? new TabAnAction(parentTab, tabActionBO) : new TabToggleAction(parentTab, tabActionBO));
        List<TabActionBO> childList = tabActionBO.getChildList();
        if (CollectionUtils.isNotEmpty(childList)) {
            childList.forEach(f -> initAction(tabActionBO.getTitle(), f));
        }
        return this;
    }


    /**
     * 构建一个批量编辑的动作参数
     *
     * @param bulkEditPanel  批量编辑主面板
     * @param tabBarListener 批量编辑动作监听器
     * @return 批量编辑动作参数对象
     */
    private TabActionBO buildBulkEditTab(JPanel bulkEditPanel, TabBarListener tabBarListener) {
        return new TabActionBO(BULK_EDIT, FuDocIcons.FU_REQUEST_BULK_EDIT, bulkEditPanel, tabBarListener, ActionType.AN_ACTION);
    }


    /**
     * 切换当前展示的tab
     *
     * @param tab tab名称
     * @return 当前组件
     */
    public FuDocTabComponent switchTab(String tab) {
        TabActionBO tabActionBO = tabActionMap.get(tab);
        if (Objects.nonNull(tabActionBO)) {
            switchTab(DEFAULT, tabActionBO);
        }
        return this;
    }


    /**
     * 切换当前展示的子tab
     *
     * @param parentTab 父tab title
     * @param tab       当前子tab title
     * @return 当前组件
     */
    public FuDocTabComponent switchTab(String parentTab, String tab) {
        TabActionBO tabActionBO = tabActionMap.get(parentTab);
        if (Objects.nonNull(tabActionBO)) {
            List<TabActionBO> childList = tabActionBO.getChildList();
            if (CollectionUtils.isNotEmpty(childList)) {
                childList.stream().filter(f -> f.getTitle().equals(tab)).findFirst().ifPresent(f -> switchTab(parentTab, f));
            }
        }
        return this;
    }


    /**
     * 切换二级tab
     *
     * @param parentTab   一级tab的title
     * @param tabActionBO 二级tab参数
     */
    public void switchTab(String parentTab, TabActionBO tabActionBO) {
        currentTabMap.put(parentTab, tabActionBO.getTitle());
        tabActionBO.setSelect(true);
        JComponent switchComponent = tabActionBO.getMainComponent();
        if (ActionType.AN_ACTION.equals(tabActionBO.getActionType()) && !tabActionBO.isSelect()) {
            //切换tab主面板 没有选中时需要切换到未选中时的面板
            switchComponent = getDefaultComponent(parentTab);
        }
        switchPanel(switchComponent);
        TabBarListener tabBarListener = tabActionBO.getTabBarListener();
        if (Objects.nonNull(tabBarListener)) {
            if (ActionType.AN_ACTION.equals(tabActionBO.getActionType())) {
                tabBarListener.onClick(tabActionBO);
            } else {
                tabBarListener.onSelect(tabActionBO);
            }
        }
        if (DEFAULT.equals(parentTab)) {
            //如果是一级导航栏 则还需要切换右侧二级导航栏
            switchToolBarPanel(tabActionBO.getChildPanel());
        }
    }


    private JComponent getDefaultComponent(String tab) {
        if (DEFAULT.equals(tab)) {
            return this.mainComponent;
        }
        TabActionBO tabActionBO = tabActionMap.get(tab);
        if (Objects.nonNull(tabActionBO)) {
            return tabActionBO.getMainComponent();
        }
        return new JPanel(new BorderLayout());
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

    /**
     * 切换面板
     *
     * @param switchPanel 需要切换的面板
     */
    private void switchToolBarPanel(JComponent switchPanel) {
        this.toolBarRightPanel.removeAll();
        this.toolBarRightPanel.repaint();
        if (Objects.nonNull(switchPanel)) {
            this.toolBarRightPanel.add(switchPanel, BorderLayout.CENTER);
        }
        this.toolBarRightPanel.revalidate();
    }


    class TabAnAction extends AnAction {
        private final String parentTitle;

        private final TabActionBO tabActionBO;

        public TabAnAction(String parentTitle, TabActionBO tabActionBO) {
            super(tabActionBO.getTitle(), tabActionBO.getTitle(), tabActionBO.getIcon());
            this.parentTitle = parentTitle;
            this.tabActionBO = tabActionBO;
        }

        @Override
        public void actionPerformed(@NotNull AnActionEvent e) {
            tabActionBO.setSelect(!tabActionBO.isSelect());
            //选中时 切换到当前动作的主面板
            switchTab(parentTitle, tabActionBO);
        }
    }


    class TabToggleAction extends ToggleAction {
        private final String parentTitle;

        private final TabActionBO tabActionBO;

        public TabToggleAction(String parentTitle, TabActionBO tabActionBO) {
            super(tabActionBO.getTitle(), tabActionBO.getTitle(), tabActionBO.getIcon());
            this.parentTitle = parentTitle;
            this.tabActionBO = tabActionBO;
        }

        @Override
        public boolean isDumbAware() {
            return true;
        }

        @Override
        public boolean isSelected(@NotNull AnActionEvent e) {
            String currentTab = currentTabMap.get(parentTitle);
            return tabActionBO.getTitle().equals(currentTab);
        }

        @Override
        public void setSelected(@NotNull AnActionEvent e, boolean state) {
            String currentTab = currentTabMap.get(parentTitle);
            String clickTab = tabActionBO.getTitle();
            if (!clickTab.equals(currentTab)) {
                //选中时 切换到当前动作的主面板
                switchTab(parentTitle, tabActionBO);
            }

        }
    }

}
