package com.wdf.fudoc.request.constants.enumtype;

import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2023-06-04 22:03:12
 */
@Getter
public enum ScriptCmdType {
    DEMO("demo","脚本编写教程",1),
    SCRIPT_DEMO("script_demo","设置示例",2),
    HTTP("http","http请求",3),
    LOG("log","日志打印",4),
    ;

    private final String type;

    private final String desc;

    private final Integer sort;

    ScriptCmdType(String type, String desc, Integer sort) {
        this.type = type;
        this.desc = desc;
        this.sort = sort;
    }
}
