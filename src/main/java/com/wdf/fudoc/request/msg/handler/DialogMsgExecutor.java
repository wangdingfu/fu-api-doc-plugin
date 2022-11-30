package com.wdf.fudoc.request.msg.handler;

import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.request.constants.enumtype.MessageType;

/**
 * @author wangdingfu
 * @date 2022-11-30 17:43:46
 */
public class DialogMsgExecutor implements FuMsgExecutor {
    @Override
    public MessageType getMessageType() {
        return MessageType.DIALOG;
    }

    @Override
    public void execute(String msgId, FuMsgItemBO fuMsgItemBO) {
        System.out.println("123");
    }
}
