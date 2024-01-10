package com.wdf.fudoc.components.widget;

import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.util.FuStringUtils;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2023-02-01 10:45:04
 */
public interface FuWidget {

    default boolean isRight() {
        return true;
    }

    JComponent getComponent();

    default void initData(FuHttpRequestData fuHttpRequestData) {

    }

    default String getCurrent() {
        return FuStringUtils.EMPTY;
    }

    default boolean isShow() {
        return true;
    }


    default void refresh() {

    }
}
