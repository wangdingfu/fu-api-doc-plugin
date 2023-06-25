package com.wdf.fudoc.request.constants.enumtype;

import org.jetbrains.annotations.NonNls;

/**
 * @author wangdingfu
 * @date 2023-06-25 16:12:51
 */
public enum ViewMode {
    SINGLE_PINNED("single"),
    MULTI_PINNED("multi"),
    ;

    public final String myActionID;

    ViewMode(@NonNls String actionID) {
        myActionID = actionID;
    }

}
