package com.wdf.fudoc.request.http.action;

import com.intellij.openapi.actionSystem.ActionUpdateThread;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import org.jetbrains.annotations.NotNull;

/**
 * 发起http请求
 *
 * @author wangdingfu
 * @date 2023-05-19 22:41:12
 */
public class RequestHttpAction extends AnAction {

    @Override
    public @NotNull ActionUpdateThread getActionUpdateThread() {
        return ActionUpdateThread.BGT;
    }


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

    }
}
