package com.wdf.fudoc.util;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.ActionManager;
import com.intellij.openapi.actionSystem.ActionToolbar;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-09-17 18:57:30
 */
public class ToolBarUtils {

    public static void genToolBarPanel(JPanel toolBarPanel, String place, ActionGroup actionGroup, String layout) {
        ActionToolbarImpl toolbar = (ActionToolbarImpl) ActionManager.getInstance().createActionToolbar(place, actionGroup, true);
        toolbar.setTargetComponent(toolBarPanel);
        toolbar.setForceMinimumSize(true);
        toolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
        Utils.setSmallerFontForChildren(toolbar);
        toolbar.getComponent().setBackground(toolBarPanel.getBackground());
        toolBarPanel.add(toolbar.getComponent(), layout);
    }
}
