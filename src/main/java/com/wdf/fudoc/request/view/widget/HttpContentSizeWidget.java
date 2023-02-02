package com.wdf.fudoc.request.view.widget;

import com.intellij.ui.HyperlinkLabel;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.message.FuMessageComponent;
import com.wdf.fudoc.components.message.FuMsgBuilder;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2023-02-01 11:25:35
 */
public class HttpContentSizeWidget implements FuWidget {

    private final FuMessageComponent fuMessageComponent;

    public HttpContentSizeWidget() {
        this.fuMessageComponent = new FuMessageComponent();
    }

    @Override
    public JComponent getComponent() {
        return this.fuMessageComponent;
    }

    @Override
    public void initData(FuHttpRequestData fuHttpRequestData) {
        this.fuMessageComponent.setMsg(FuMsgBuilder.getInstance().text("Size: ").text("806 B", FuColor.GREEN).build());
    }
}
