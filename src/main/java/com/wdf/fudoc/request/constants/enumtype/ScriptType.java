package com.wdf.fudoc.request.constants.enumtype;

import com.wdf.fudoc.common.FuBundle;
import lombok.Getter;

/**
 * @author wangdingfu
 * @date 2023-07-23 15:01:46
 */
@Getter
public enum ScriptType {

    PRE_SCRIPT(FuBundle.message("fudoc.script.pre.title")),

    POST_SCRIPT(FuBundle.message("fudoc.script.post.title"));

    private final String view;

    ScriptType(String view) {
        this.view = view;
    }
}
