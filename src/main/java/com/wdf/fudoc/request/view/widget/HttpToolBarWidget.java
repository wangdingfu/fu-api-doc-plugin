package com.wdf.fudoc.request.view.widget;

import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2023-05-28 14:25:01
 */
public class HttpToolBarWidget implements FuWidget {

    private final JPanel toolBarPanel;

    @Override
    public boolean isRight() {
        return true;
    }

    public HttpToolBarWidget(JPanel toolBarPanel) {
        this.toolBarPanel = toolBarPanel;
    }

    @Override
    public JComponent getComponent() {
        return this.toolBarPanel;
    }

    @Override
    public void initData(FuHttpRequestData fuHttpRequestData) {

    }
}
