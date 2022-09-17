package com.wdf.fudoc.test.action.windows;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.wdf.fudoc.apidoc.config.DefaultConfig;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2022-09-07 21:04:28
 */
public class FuRequestSettingAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        DefaultConfig.sendStatus = !DefaultConfig.sendStatus;

    }
}
