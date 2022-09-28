package com.wdf.fudoc.components.toolbar;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.wdf.fudoc.request.global.GlobalHttpRequestView;
import com.wdf.fudoc.request.view.HttpDialogView;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2022-09-28 13:43:07
 */
public class ExistToolBarAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        //关闭弹窗
        HttpDialogView httpDialogView = GlobalHttpRequestView.getHttpDialogView(e.getProject());
        //关闭弹窗
        httpDialogView.close();
        GlobalHttpRequestView.remove(e.getProject());

    }
}
