package com.wdf.fudoc.request.msg.handler;

import com.intellij.ide.BrowserUtil;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.request.constants.enumtype.MessageType;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdingfu
 * @date 2022-11-30 17:37:20
 */
public class LinkMsgExecutor implements FuMsgExecutor {

    @Override
    public MessageType getMessageType() {
        return MessageType.LINK;
    }

    @Override
    public void execute(String msgId, FuMsgItemBO fuMsgItemBO) {
        String value = fuMsgItemBO.getValue();
        if(StringUtils.isNotBlank(value)){
            BrowserUtil.browse(value);
        }
    }
}
