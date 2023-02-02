package com.wdf.fudoc.test.action;

import com.intellij.openapi.actionSystem.*;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.apidoc.sync.data.YApiProjectTableData;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.view.FuRequestStatusInfoView;
import com.wdf.fudoc.test.view.TestTipPanel;
import com.wdf.fudoc.util.PopupUtils;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        FuRequestStatusInfoView fuRequestStatusInfoView = new FuRequestStatusInfoView();
        FuHttpRequestData fuHttpRequestData = new FuHttpRequestData();
        fuHttpRequestData.setHttpCode(200);
        fuHttpRequestData.setTime(1045L);
        fuRequestStatusInfoView.initData(fuHttpRequestData);
        JPanel rootPanel = fuRequestStatusInfoView.getRootPanel();
        UIUtil.applyStyle(UIUtil.ComponentStyle.SMALL, rootPanel);
        PopupUtils.create(rootPanel,null,new AtomicBoolean(true));
    }


}
