package com.wdf.fudoc.request.view.widget;

import com.intellij.ui.HyperlinkLabel;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.message.FuMessageComponent;
import com.wdf.fudoc.components.message.FuMsgBuilder;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;

import javax.swing.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-02-01 11:24:45
 */
public class HttpTimeWidget implements FuWidget {

    private final FuMessageComponent fuMessageComponent;

    public HttpTimeWidget() {
        this.fuMessageComponent = new FuMessageComponent();
    }

    @Override
    public JComponent getComponent() {
        return this.fuMessageComponent;
    }

    @Override
    public void initData(FuHttpRequestData fuHttpRequestData) {
        Long time = fuHttpRequestData.getTime();
        if (Objects.isNull(time)) {
            return;
        }
        fuMessageComponent.setMsg(FuMsgBuilder.getInstance().text("Time: ").text(time + " ms", FuColor.GREEN).build());

    }
}
