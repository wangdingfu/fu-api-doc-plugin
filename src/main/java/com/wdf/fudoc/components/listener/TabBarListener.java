package com.wdf.fudoc.components.listener;

import com.wdf.fudoc.components.bo.TabActionBO;
import com.wdf.fudoc.test.view.bo.BarPanelBO;

/**
 * @author wangdingfu
 * @date 2022-09-19 17:31:53
 */
public interface TabBarListener {

    /**
     * bar点击事件
     *
     * @param barPanelBO 工具栏面板对象
     */
    default void click(BarPanelBO barPanelBO) {

    }

    /**
     * bar点击事件
     *
     * @param tabActionBO 工具栏面板对象
     */
    default void onClick(TabActionBO tabActionBO) {

    }


    /**
     * toggle bar 选中事件
     *
     * @param state 选中状态
     */
    default void select(boolean state) {

    }

    /**
     * toggle bar 选中事件
     *
     * @param tabActionBO 选中tab的参数
     */
    default void onSelect(TabActionBO tabActionBO) {

    }

}
