package com.wdf.fudoc.request.constants.enumtype;

import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2023-06-04 22:03:12
 */
@Getter
public enum ScriptCmdType {
    DEMO("demo","脚本编写教程"),
    SCRIPT_DEMO("script_demo","设置示例"),
    LOG("log","日志打印"),
    ;

    private final String type;

    private final String desc;

    ScriptCmdType(String type, String desc) {
        this.type = type;
        this.desc = desc;
    }
}
