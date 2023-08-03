package com.wdf.fudoc.request.constants.enumtype;

import org.jetbrains.annotations.NonNls;

/**
 * @author wangdingfu
 * @date 2023-06-25 16:12:51
 */
public enum ViewMode {
    SINGLE_PINNED("single","始终只展示一个"),
    MULTI_PINNED("multi","可展示多个"),
    ;

    public final String myActionID;
    public final String myActionName;

    ViewMode(String myActionID, String myActionName) {
        this.myActionID = myActionID;
        this.myActionName = myActionName;
    }
}
