package com.wdf.fudoc.request.constants.enumtype;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * 消息类型
 *
 * @author wangdingfu
 * @date 2022-11-30 14:29:24
 */
@Getter
public enum MessageType {

    NORMAL("normal"),
    LINK("link"),
    DIALOG("dialog");


    private final String code;

    MessageType(String code) {
        this.code = code;
    }

    public static MessageType getEnum(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (MessageType value : MessageType.values()) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
        }
        return null;
    }


    public static boolean isUnNormal(String code){
        return !MessageType.NORMAL.getCode().equals(code);
    }


}
