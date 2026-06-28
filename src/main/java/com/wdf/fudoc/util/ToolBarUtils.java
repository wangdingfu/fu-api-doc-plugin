package com.wdf.fudoc.util;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;

import javax.swing.*;
import java.awt.*;

/**
 * @author wangdingfu
 * @date 2022-09-17 18:57:30
 */
public class ToolBarUtils {


    /**
     * 将一组动作添加到工具栏中展示
     *
     * @param toolBarPanel 工具栏面板
     * @param place        工具栏面板别名
     * @param actionGroup  工具栏动作分组
     * @param layout       工具栏面板布局
     */
    public static void addActionToToolBar(JPanel toolBarPanel, String place, ActionGroup actionGroup, String layout) {
        toolBarPanel.add(addActionToToolBar(toolBarPanel, place, actionGroup), layout);
    }


    public static JComponent addActionToToolBar(JComponent targetComponent, String place, ActionGroup actionGroup) {
        // 标准创建，不再强转为内部实现类 ActionToolbarImpl
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(place, actionGroup, true);

        toolbar.setTargetComponent(targetComponent);

        JComponent toolbarComp = toolbar.getComponent();
        Utils.setSmallerFontForChildren(toolbarComp);
        toolbarComp.setBackground(targetComponent.getBackground());

        return toolbarComp;
    }


    /**
     * 生成工具栏面板
     *
     * @param place       工具栏面板别名
     * @param actionGroup 工具栏动作分组
     * @param layout      工具栏面板布局
     * @return 工具栏面板
     */
    public static JPanel genToolBarPanel(String place, ActionGroup actionGroup, String layout) {
        JPanel toolBarPanel = new JPanel(new BorderLayout());
        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(place, actionGroup, true);
        toolbar.setTargetComponent(toolBarPanel);
        // Note: setLayoutPolicy() is deprecated and removed in IDEA 2025.1+
        // The default behavior is already NOWRAP, so this call is not needed
        Utils.setSmallerFontForChildren(toolbar.getComponent());
        toolBarPanel.add(toolbar.getComponent(), layout);
        return toolBarPanel;
    }


}
