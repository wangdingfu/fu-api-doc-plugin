package com.wdf.fudoc.request.msg.handler;

import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.request.constants.enumtype.MessageType;

/**
 * @author wangdingfu
 * @date 2022-11-30 17:31:50
 */
public interface FuMsgExecutor {


    /**
     * 获取消息类型
     */
    MessageType getMessageType();


    /**
     * 消息点击后触发事件
     *
     * @param msgId       消息id
     * @param fuMsgItemBO 点击的消息对象
     */
    void execute(String msgId, FuMsgItemBO fuMsgItemBO);
}
