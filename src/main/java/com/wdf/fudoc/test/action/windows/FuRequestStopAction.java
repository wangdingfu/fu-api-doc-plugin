package com.wdf.fudoc.test.action.windows;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.wdf.fudoc.apidoc.config.DefaultConfig;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2022-09-07 21:04:28
 */
public class FuRequestStopAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(false);
        if (DefaultConfig.sendStatus) {
            presentation.setVisible(false);
        }
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
    }
}
