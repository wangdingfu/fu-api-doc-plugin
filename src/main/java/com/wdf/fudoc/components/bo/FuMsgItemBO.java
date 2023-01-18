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
     * 常规主题展示的颜色（白色主题）
     */
    private String regularColor;

    /**
     * 黑色主题展示的颜色
     */
    private String darkColor;
}
