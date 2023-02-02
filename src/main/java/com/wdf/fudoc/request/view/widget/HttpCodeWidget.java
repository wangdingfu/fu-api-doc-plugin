package com.wdf.fudoc.request.view.widget;

import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.message.FuMessageComponent;
import com.wdf.fudoc.components.message.FuMsgBuilder;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;

import javax.swing.*;
import java.util.Objects;

/**
 * http状态码展示
 *
 * @author wangdingfu
 * @date 2023-02-01 11:12:11
 */
public class HttpCodeWidget implements FuWidget {

    private final FuMessageComponent fuMessageComponent;

    public HttpCodeWidget() {
        this.fuMessageComponent = new FuMessageComponent();
    }

    @Override
    public JComponent getComponent() {
        return this.fuMessageComponent;
    }

    @Override
    public void initData(FuHttpRequestData fuHttpRequestData) {
        Integer status;
        if (Objects.isNull(status = fuHttpRequestData.getHttpCode())) {
            return;
        }
        boolean isOk = status.equals(200);
        fuMessageComponent.setMsg(FuMsgBuilder.getInstance().text("Status: ").text(isOk ? status + " OK" : status + "", isOk ? FuColor.GREEN : FuColor.RED).build());
    }


}
