package com.wdf.fudoc.request.action;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.wdf.fudoc.request.view.HttpDialogView;
import org.jetbrains.annotations.NotNull;

/**
 * 弹出http请求窗口
 *
 * @author wangdingfu
 * @date 2022-09-17 18:01:04
 */
public class RequestAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        HttpDialogView.popup(e.getProject());
    }
}
