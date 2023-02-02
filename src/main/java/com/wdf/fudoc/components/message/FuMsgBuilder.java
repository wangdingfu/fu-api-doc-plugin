package com.wdf.fudoc.components.message;

import cn.hutool.core.util.IdUtil;
import com.google.common.collect.Lists;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.bo.FuMsgBO;
import com.wdf.fudoc.components.bo.FuMsgItemBO;
import com.wdf.fudoc.request.constants.enumtype.MessageType;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-02-01 13:53:06
 */
public class FuMsgBuilder {

    private final FuMsgBO fuMsgBO;

    public FuMsgBuilder() {
        this.fuMsgBO = new FuMsgBO();
        this.fuMsgBO.setMsgId(IdUtil.nanoId());
        this.fuMsgBO.setWeight(1D);
        this.fuMsgBO.setItemList(Lists.newArrayList());
    }

    public static FuMsgBuilder getInstance() {
        return new FuMsgBuilder();
    }

    public FuMsgBO build() {
        return this.fuMsgBO;
    }

    public FuMsgBuilder text(String text) {
        return text(text, MessageType.NORMAL, null, null);
    }


    public FuMsgBuilder text(String text, FuColor fuColor) {
        return text(text, MessageType.NORMAL, fuColor, null);
    }

    public FuMsgBuilder linkText(String text, FuColor fuColor, String value) {
        return text(text, MessageType.LINK, fuColor, value);
    }

    public FuMsgBuilder text(String text, MessageType messageType, FuColor fuColor, String value) {
        FuMsgItemBO fuMsgItemBO = new FuMsgItemBO();
        fuMsgItemBO.setText(text);
        if (Objects.isNull(messageType)) {
            messageType = MessageType.NORMAL;
        }
        fuMsgItemBO.setMsgType(messageType.getCode());
        if (Objects.nonNull(fuColor)) {
            fuMsgItemBO.setDarkColor(fuColor.getDarkColor());
            fuMsgItemBO.setRegularColor(fuColor.getRegularColor());
        }
        fuMsgItemBO.setMsgId(IdUtil.nanoId());
        fuMsgItemBO.setValue(value);
        this.fuMsgBO.add(fuMsgItemBO);
        return this;
    }
}
