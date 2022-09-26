package com.wdf.fudoc.test.action;

import com.intellij.openapi.actionSystem.*;
import com.wdf.fudoc.request.view.ResponseErrorView;
import com.wdf.fudoc.util.PopupUtils;
import org.jetbrains.annotations.NotNull;

public class TestAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        ResponseErrorView responseErrorView = new ResponseErrorView();
        PopupUtils.create(responseErrorView.getRootPanel(),null,null);
    }




}
