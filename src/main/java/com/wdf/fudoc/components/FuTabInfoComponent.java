//package com.wdf.fudoc.components;
//
//import com.intellij.find.editorHeaderActions.Utils;
//import com.intellij.openapi.actionSystem.*;
//import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
//import com.intellij.ui.tabs.TabInfo;
//import com.wdf.fudoc.apidoc.constant.enumtype.ActionType;
//import com.wdf.fudoc.components.bo.TabActionBO;
//import com.wdf.fudoc.components.listener.TabBarListener;
//import icons.FuDocIcons;
//import lombok.Getter;
//import lombok.Setter;
//import org.apache.commons.collections.CollectionUtils;
//import org.apache.commons.lang3.StringUtils;
//import org.jetbrains.annotations.NotNull;
//
//import javax.swing.*;
//import java.awt.*;
//import java.util.List;
//import java.util.Map;
//import java.util.Objects;
//import java.util.concurrent.ConcurrentHashMap;
//
///**
// * tab编辑器组件
// * 支持tab右侧添加工具栏
// *
// * @author wangdingfu
// * @date 2022-11-05 08:31:14
// */
//@Getter
//@Setter
//public class FuTabInfoComponent {
//
//
//    private static final String DEFAULT = "DEFAULT";
//    private static final String BULK_EDIT = "BULK_EDIT";
//
//    /**
//     * 根面板 最终展示内容的面板
//     */
//    private JPanel rootPanel;
//
//    /**
//     * tab标题
//     */
//    private String title;
//    /**
//     * tab图标
//     */
//    private Icon icon;
//
//    /**
//     * 当前tab页面展示的组件
//     */
//    private JComponent mainComponent;
//
//    /**
//     * 动作组（一级导航栏）
//     */
//    private DefaultActionGroup actionGroup = new DefaultActionGroup();
//
//    /**
//     * 当前的tab 默认key为上一级tab名称 value为下一级正在展示的tab
//     */
//    private Map<String, String> currentTabMap = new ConcurrentHashMap<>();
//
//    /**
//     * 每一个tab动作参数
//     */
//    private Map<String, TabActionBO> tabActionMap = new ConcurrentHashMap<>();
//
//
//    private FuTabInfoComponent(String title, Icon icon, JComponent rootPanel) {
//        this.title = title;
//        this.icon = icon;
//        this.mainComponent = rootPanel;
//    }
//
//    /**
//     * 构建一个右侧有工具栏的tab组件
//     *
//     * @param title     tab标题
//     * @param icon      tab图标
//     * @param mainPanel tab主面板
//     * @return tab组件
//     */
//    public static FuTabInfoComponent getInstance(String title, Icon icon, JComponent mainPanel) {
//        return new FuTabInfoComponent(title, icon, mainPanel);
//    }
//
//
//    /**
//     * 构建成TabInfo对象
//     */
//    public TabInfo builder() {
//        TabInfo tabInfo = new TabInfo(this.rootPanel);
//        if (Objects.nonNull(this.icon)) {
//            tabInfo.setIcon(this.icon);
//        }
//
//        tabInfo.setText(this.title);
//        return tabInfo;
//    }
//
//    public void genToolBarPanel(JPanel toolBarPanel, ActionGroup actionGroup, String layout) {
//        ActionToolbarImpl toolbar = (ActionToolbarImpl) ActionManager.getInstance().createActionToolbar("fudoc.request.toggle.bar", actionGroup, true);
//        toolbar.setTargetComponent(toolBarPanel);
//        toolbar.setForceMinimumSize(true);
//        toolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
//        Utils.setSmallerFontForChildren(toolbar);
//        toolBarPanel.add(toolbar.getComponent(), layout);
//    }
//
//
//
//
//    /**
//     * 往tab右侧的工具栏中添加一个动作
//     *
//     * @param title       动作名称
//     * @param icon        动作图标
//     * @param actionPanel 当前动作触发后切换到指定的面板
//     * @return tab组件对象
//     */
//    public FuTabInfoComponent addAction(String title, Icon icon, JPanel actionPanel) {
//        return addAction(title, icon, actionPanel, null, null);
//    }
//
//
//    /**
//     * 往tab右侧的工具栏中添加批量编辑按钮
//     *
//     * @param actionPanel 点击批量编辑按钮切换的面板
//     * @return tab组件对象
//     */
//    public FuTabInfoComponent addBulkEditBar(JPanel actionPanel) {
//        addToActionGroup(buildBulkEditTab(actionPanel, null));
//        return this;
//    }
//
//
//    /**
//     * 往tab右侧的工具栏中添加一个按钮 并且针对该按钮添加一个批量编辑按钮
//     *
//     * @param title          按钮名称
//     * @param icon           按钮图标
//     * @param actionPanel    点击按钮切换到指定的面板
//     * @param bulkEditPanel  批量编辑面板(将批量编辑面板挂在当前工具栏下 点击当前工具栏会出现对应批量编辑按钮进入批量编辑面板 类似二级菜单效果)
//     * @param tabBarListener 批量编辑面板按钮监听器 当点击批量编辑按钮时触发 告诉调用者当前点击了编辑按钮
//     * @return tab组件对象
//     */
//    public FuTabInfoComponent addAction(String title, Icon icon, JPanel actionPanel, JPanel bulkEditPanel, TabBarListener tabBarListener) {
//        TabActionBO tabActionBO = new TabActionBO(title, icon, actionPanel, ActionType.TOGGLE_ACTION);
//        if (Objects.nonNull(bulkEditPanel)) {
//            tabActionBO.addChild(buildBulkEditTab(bulkEditPanel, tabBarListener));
//        }
//        //将当前动作添加到动作组中
//        addToActionGroup(tabActionBO);
//        return this;
//    }
//
//    /**
//     * 添加一级动作到动作组中
//     *
//     * @param tabActionBO 动作对象
//     */
//    public void addToActionGroup(TabActionBO tabActionBO) {
//        addToActionGroup(this.actionGroup, DEFAULT, tabActionBO);
//    }
//
//    /**
//     * 添加一个动作到动作组中
//     *
//     * @param actionKey   动作的key
//     * @param tabActionBO 动作对象
//     */
//    private void addToActionGroup(DefaultActionGroup actionGroup, String actionKey, TabActionBO tabActionBO) {
//        if (StringUtils.isBlank(actionKey)) {
//            actionKey = DEFAULT;
//        }
//        tabActionMap.put(buildActionKey(actionKey, tabActionBO.getTitle()), tabActionBO);
//        ActionType actionType = tabActionBO.getActionType();
//        actionGroup.add(ActionType.AN_ACTION.equals(actionType) ? new TabAnAction(actionKey, tabActionBO) : new TabToggleAction(actionKey, tabActionBO));
//        //将子动作添加到动作组中
//        List<TabActionBO> childList = tabActionBO.getChildList();
//        if (CollectionUtils.isNotEmpty(childList)) {
//            DefaultActionGroup childActionGroup = new DefaultActionGroup();
//            for (TabActionBO actionBO : childList) {
//                addToActionGroup(childActionGroup, tabActionBO.getTitle(), actionBO);
//            }
//            tabActionBO.setChildActionGroup(childActionGroup);
//        }
//    }
//
//
//    /**
//     * 构建一个唯一的动作key
//     *
//     * @param actionGroup 当前动作所属的分组 一级动作默认为DEFAULT, 二级动作为父动作的title
//     * @param actionTitle 当前动作的title
//     * @return 唯一动作标识
//     */
//    private String buildActionKey(String actionGroup, String actionTitle) {
//        return actionGroup + ":" + actionTitle;
//    }
//
//
//    /**
//     * 构建一个批量编辑的动作参数
//     *
//     * @param bulkEditPanel  批量编辑主面板
//     * @param tabBarListener 批量编辑动作监听器
//     * @return 批量编辑动作参数对象
//     */
//    private TabActionBO buildBulkEditTab(JPanel bulkEditPanel, TabBarListener tabBarListener) {
//        return new TabActionBO(BULK_EDIT, FuDocIcons.FU_REQUEST_BULK_EDIT, bulkEditPanel, tabBarListener, ActionType.AN_ACTION);
//    }
//
//
//
//    private void switchPanel(String actionKey, TabActionBO tabActionBO, boolean state) {
//        currentTabMap.put(actionKey, tabActionBO.getTitle());
//        tabActionBO.setSelect(true);
//        switchPanel(tabActionBO.getMainComponent());
//        TabBarListener tabBarListener = tabActionBO.getTabBarListener();
//        if (Objects.nonNull(tabBarListener)) {
//            if (ActionType.AN_ACTION.equals(tabActionBO.getActionType())) {
//                tabBarListener.onClick(tabActionBO);
//            } else {
//                tabBarListener.select(state);
//            }
//        }
//    }
//
//    /**
//     * 切换面板
//     *
//     * @param switchPanel 需要切换的面板
//     */
//    private void switchPanel(JComponent switchPanel) {
//        this.rootPanel.removeAll();
//        this.rootPanel.repaint();
//        this.rootPanel.add(switchPanel, BorderLayout.CENTER);
//        this.rootPanel.revalidate();
//    }
//}
