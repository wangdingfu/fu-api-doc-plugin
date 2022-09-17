package com.wdf.fudoc.test.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.wdf.fudoc.util.PopupUtils;
import com.wdf.fudoc.components.FuTableEditorComponent;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2022-09-05 19:39:54
 */
public class TestTableAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PopupUtils.popup(FuTableEditorComponent.createUIComponents());
    }
}
