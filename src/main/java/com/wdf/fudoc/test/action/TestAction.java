package com.wdf.fudoc.test.action;

import com.intellij.openapi.actionSystem.*;
import com.wdf.fudoc.request.view.ResponseErrorView;
import com.wdf.fudoc.test.view.TestRequestFrom;
import com.wdf.fudoc.util.PopupUtils;
import org.jetbrains.annotations.NotNull;

public class TestAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        PopupUtils.create(new TestRequestFrom(e.getProject()).getRootPanel(),null,null);
    }




}
