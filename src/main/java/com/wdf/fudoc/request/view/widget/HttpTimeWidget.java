package com.wdf.fudoc.request.view.widget;

import cn.fudoc.common.enumtype.FuColor;
import cn.fudoc.common.msg.bo.FuMsgBO;
import com.wdf.fudoc.components.message.FuMessageComponent;
import cn.fudoc.common.msg.FuMsgBuilder;
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
        FuMsgBO fuMsgBO = null;
        Long time = fuHttpRequestData.getTime();
        if (Objects.nonNull(time)) {
            fuMsgBO = FuMsgBuilder.getInstance().text("Time: ").text(time + " ms", FuColor.GREEN).build();
        }
        fuMessageComponent.setMsg(fuMsgBO);

    }
}
