package com.wdf.fudoc.components.toolbar;

import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.wdf.fudoc.request.global.GlobalHttpRequestView;
import com.wdf.fudoc.request.view.HttpDialogView;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-09-28 13:43:07
 */
public class ExistToolBarAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        HttpDialogView httpDialogView = GlobalHttpRequestView.getHttpDialogView(e.getProject());
        if (Objects.nonNull(httpDialogView)) {
            //关闭弹窗
            httpDialogView.close();
            GlobalHttpRequestView.remove(e.getProject());
        }
    }
}
