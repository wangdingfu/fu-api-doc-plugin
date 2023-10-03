package com.wdf.fudoc.components.message.handler;

import com.intellij.ide.BrowserUtil;
import com.wdf.api.msg.bo.FuMsgItemBO;
import com.wdf.api.enumtype.MessageType;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdingfu
 * @date 2022-11-30 17:37:20
 */
public class LinkMsgExecutor implements FuMsgExecutor {


    @Override
    public void execute(String msgId, FuMsgItemBO fuMsgItemBO) {
        String value = fuMsgItemBO.getValue();
        if(StringUtils.isNotBlank(value)){
            BrowserUtil.browse(value);
        }
    }
}
