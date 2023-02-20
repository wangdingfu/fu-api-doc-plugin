package com.wdf.fudoc.test.action;

import com.intellij.ide.highlighter.JavaFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.editor.impl.EditorComponentImpl;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.apidoc.sync.data.YApiProjectTableData;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.AuthSettingView;
import com.wdf.fudoc.request.view.FuRequestStatusInfoView;
import com.wdf.fudoc.test.view.TestTipPanel;
import com.wdf.fudoc.util.PopupUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestAction extends AnAction {




    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        AuthSettingView authSettingView = new AuthSettingView(e.getProject());
        PopupUtils.create(authSettingView.getRootPanel(),null,new AtomicBoolean(true));
    }


}
