package com.wdf.fudoc.apidoc.constant.enumtype;

import lombok.Getter;

/**
 * @author wangdingfu
 * @descption: 是 或则 否
 * @date 2022-05-22 22:33:52
 */
@Getter
public enum YesOrNo {
    NO(0,"否"),
    YES(1,"是")
    ;

    private final int code;

    private final String desc;

    YesOrNo(int code, String desc) {
        this.code = code;
        this.desc = desc;
    }
}
