package com.wdf.fudoc.components.message;

import com.google.common.collect.Lists;
import cn.fudoc.common.enumtype.FuColor;
import cn.fudoc.common.msg.bo.FuMsgBO;
import cn.fudoc.common.msg.bo.FuMsgItemBO;
import cn.fudoc.common.enumtype.MessageType;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;

/**
 * @author wangdingfu
 * @date 2023-01-31 18:47:24
 */
public class ResponseInfoMessageGenerator {

    public static FuMsgBO buildMsg(FuHttpRequestData httpRequestData) {
        FuResponseData response = httpRequestData.getResponse();
        return buildMsg(response.getStatus() + "", httpRequestData.getTime(), response.getContentLength() + "B");
    }

    public static FuMsgBO buildMsg(String code, Long time, String size) {
        FuMsgBO fuMsgBO = new FuMsgBO();
        fuMsgBO.setMsgId("fudoc.request.http.info");
        fuMsgBO.setWeight(1d);
        FuColor fuColor = "200".equals(code) ? FuColor.GREEN : FuColor.RED;
        fuMsgBO.setItemList(Lists.newArrayList(
                //响应状态码
                buildStatusTitle(), buildStatusCode(code, fuColor),
                //耗时
                buildTimeTitle(), buildTime(time, fuColor),
                //响应内容大小
                buildSizeTitle(), buildSize(size, fuColor)));
        return fuMsgBO;
    }


    private static FuMsgItemBO buildStatusTitle() {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setText("   Status: ");
        fuMsgItemBO.setMsgId("fudoc.request.http.info.status");
        fuMsgItemBO.setMsgType(MessageType.NORMAL.getCode());
        return fuMsgItemBO;
    }

    private static FuMsgItemBO buildStatusCode(String code, FuColor fuColor) {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setText(code);
        fuMsgItemBO.setMsgId("fudoc.request.http.info.status.code");
        fuMsgItemBO.setMsgType(MessageType.NORMAL.getCode());
        fuMsgItemBO.setRegularColor(fuColor.getRegularColor());
        fuMsgItemBO.setDarkColor(fuColor.getDarkColor());
        return fuMsgItemBO;
    }


    private static FuMsgItemBO buildTimeTitle() {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setText("   Time: ");
        fuMsgItemBO.setMsgId("fudoc.request.http.info.time.title");
        fuMsgItemBO.setMsgType(MessageType.NORMAL.getCode());
        return fuMsgItemBO;
    }

    private static FuMsgItemBO buildTime(Long time, FuColor fuColor) {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setText(time + " ms");
        fuMsgItemBO.setMsgId("fudoc.request.http.info.time");
        fuMsgItemBO.setMsgType(MessageType.NORMAL.getCode());
        fuMsgItemBO.setRegularColor(fuColor.getRegularColor());
        fuMsgItemBO.setDarkColor(fuColor.getDarkColor());
        return fuMsgItemBO;
    }

    private static FuMsgItemBO buildSizeTitle() {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setText("   Size: ");
        fuMsgItemBO.setMsgId("fudoc.request.http.info.size.title");
        fuMsgItemBO.setMsgType(MessageType.NORMAL.getCode());
        return fuMsgItemBO;
    }

    private static FuMsgItemBO buildSize(String size, FuColor fuColor) {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setText(size);
        fuMsgItemBO.setMsgId("fudoc.request.http.info.size");
        fuMsgItemBO.setMsgType(MessageType.NORMAL.getCode());
        fuMsgItemBO.setRegularColor(fuColor.getRegularColor());
        fuMsgItemBO.setDarkColor(fuColor.getDarkColor());
        return fuMsgItemBO;
    }

}
