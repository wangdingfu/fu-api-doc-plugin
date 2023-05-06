package com.wdf.fudoc.common;

import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.TabsListener;

import java.util.Map;

/**
 * [Fu Tab]接口 需要返回一个tab标签页
 *
 * @author wangdingfu
 * @date 2022-09-17 20:49:35
 */
public interface FuTab extends TabsListener {

    /**
     * 获取tab
     */
    TabInfo getTabInfo();


    /**
     * 重置请求参数
     *
     * @param param 请求参数
     */
    default void resetParams(Map<String, String> param) {

    }

}
