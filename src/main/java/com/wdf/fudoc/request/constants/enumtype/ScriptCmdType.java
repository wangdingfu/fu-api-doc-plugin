package com.wdf.fudoc.request.constants.enumtype;

import com.wdf.fudoc.common.FuBundle;
import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2023-06-04 22:03:12
 */
@Getter
public enum ScriptCmdType {
    DEMO("demo", FuBundle.message("fudoc.script.study.title"), 1),
    SCRIPT_DEMO("script_demo", FuBundle.message("fudoc.script.study.example"), 2),
    HTTP("http", FuBundle.message("fudoc.script.study.http"), 3),
    LOG("log", FuBundle.message("fudoc.script.study.log"), 4),
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
