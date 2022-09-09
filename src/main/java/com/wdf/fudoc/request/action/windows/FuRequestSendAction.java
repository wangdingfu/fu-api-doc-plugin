package com.wdf.fudoc.request.action.windows;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.wdf.fudoc.apidoc.config.DefaultConfig;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2022-09-07 21:01:43
 */
public class FuRequestSendAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        if (!DefaultConfig.sendStatus) {
            Presentation presentation = e.getPresentation();
            presentation.setEnabledAndVisible(false);
        }
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
    }
}
