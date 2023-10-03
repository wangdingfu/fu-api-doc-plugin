package com.wdf.fudoc.request.view.widget;

import com.wdf.fudoc.apidoc.constant.enumtype.HttpCode;
import com.wdf.api.enumtype.FuColor;
import com.wdf.api.msg.bo.FuMsgBO;
import com.wdf.fudoc.components.message.FuMessageComponent;
import com.wdf.api.msg.FuMsgBuilder;
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
        FuMsgBO fuMsgBO = null;
        if (Objects.nonNull(status = fuHttpRequestData.getHttpCode())) {
            boolean isOk = fuHttpRequestData.isOk();
            fuMsgBO = FuMsgBuilder.getInstance().text("Status: ").text(HttpCode.getMessage(status), isOk ? FuColor.GREEN : FuColor.RED).build();
        }
        fuMessageComponent.setMsg(fuMsgBO);
    }


}
