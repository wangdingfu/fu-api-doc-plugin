package com.wdf.fudoc.common.enumtype;

/**
 * @author wangdingfu
 * @date 2023-07-01 22:20:57
 */
public enum Theme {

    Darcula,
    Light;

    public boolean isDarcula() {
        return Darcula.equals(this);
    }
}
