package com.wdf.fudoc.request.action.toolbar;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.Presentation;
import com.wdf.fudoc.apidoc.config.DefaultConfig;
import com.wdf.fudoc.request.execute.FuHttpRequest;
import com.wdf.fudoc.request.global.FuRequest;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-09-07 21:04:28
 */
public class FuRequestStopAction extends AnAction {

    @Override
    public void update(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(FuRequest.getStatus(e.getProject()));
    }

    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        Presentation presentation = e.getPresentation();
        presentation.setEnabled(false);
        //停止请求
        FuHttpRequest request = FuRequest.getRequest(e.getProject());
        if (Objects.nonNull(request)) {
            request.doStop();
        }
    }
}
