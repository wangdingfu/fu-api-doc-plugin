package com.wdf.fudoc.util;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;

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
        ActionToolbarImpl toolbar = (ActionToolbarImpl) ActionManager.getInstance().createActionToolbar(place, actionGroup, true);
        toolbar.setTargetComponent(toolBarPanel);
        toolbar.setForceMinimumSize(true);
        toolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
        Utils.setSmallerFontForChildren(toolbar);
        toolbar.getComponent().setBackground(toolBarPanel.getBackground());
        toolBarPanel.add(toolbar.getComponent(), layout);
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
        ActionToolbarImpl toolbar = (ActionToolbarImpl) ActionManager.getInstance().createActionToolbar(place, actionGroup, true);
        toolbar.setTargetComponent(toolBarPanel);
        toolbar.setForceMinimumSize(true);
        toolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
        Utils.setSmallerFontForChildren(toolbar);
        toolBarPanel.add(toolbar.getComponent(), layout);
        return toolBarPanel;
    }
}
