package com.wdf.fudoc.components.listener;

import com.wdf.fudoc.components.bo.FuMsgItemBO;

/**
 * @author wangdingfu
 * @date 2022-11-30 14:43:43
 */
public interface FuMsgListener {

    /**
     * 消息点击事件
     *
     * @param msgId       主消息id
     * @param fuMsgItemBO 点击的消息
     */
    void clickMsgEvent(String msgId, FuMsgItemBO fuMsgItemBO);
}
