package com.wdf.fudoc.components.message.handler;

import com.wdf.api.msg.bo.FuMsgItemBO;

/**
 * @author wangdingfu
 * @date 2022-11-30 17:31:50
 */
public interface FuMsgExecutor {

    /**
     * 消息点击后触发事件
     *
     * @param msgId       消息id
     * @param fuMsgItemBO 点击的消息对象
     */
    void execute(String msgId, FuMsgItemBO fuMsgItemBO);
}
