package com.wdf.fudoc.common.datakey;

import com.intellij.openapi.actionSystem.DataKey;
import com.wdf.fudoc.request.view.toolwindow.FuRequestWindow;

/**
 * @author wangdingfu
 * @date 2022-12-05 22:45:48
 */
public interface FuDocDataKey {

    DataKey<FuRequestWindow> WINDOW_PANE = DataKey.create("FuRequestWindow");
}
