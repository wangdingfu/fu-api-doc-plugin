package com.wdf.fudoc.request.constants.enumtype;

import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

/**
 * @author wangdingfu
 * @date 2022-11-30 18:23:49
 */
@Getter
public enum MessageStyle {

    /**
     * 橙色样式
     */
    ORANGE("orange"),


    ;

    private final String code;

    MessageStyle(String code) {
        this.code = code;
    }

    public static MessageStyle getEnum(String code) {
        if (StringUtils.isNotBlank(code)) {
            for (MessageStyle value : MessageStyle.values()) {
                if (value.getCode().equals(code)) {
                    return value;
                }
            }
        }
        return null;
    }


}
