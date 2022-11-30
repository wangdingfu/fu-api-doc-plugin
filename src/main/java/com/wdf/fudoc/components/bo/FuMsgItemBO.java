package com.wdf.fudoc.components.bo;

import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2022-11-30 14:04:59
 */
@Getter
@Setter
public class FuMsgItemBO {


    /**
     * 消息id
     */
    private String msgId;

    /**
     * 消息类型
     */
    private String msgType;

    /**
     * 消息属性值
     */
    private String value;

    /**
     * 消息文本
     */
    private String text;

    /**
     * 消息样式(已经内置好的样式)
     */
    private String style;


}
