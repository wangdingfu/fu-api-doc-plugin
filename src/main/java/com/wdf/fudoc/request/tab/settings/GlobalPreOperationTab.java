package com.wdf.fudoc.request.tab.settings;

import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import icons.FuDocIcons;

import javax.swing.*;

/**
 * 前置操作tab
 *
 * @author wangdingfu
 * @date 2022-12-26 22:38:48
 */
public class GlobalPreOperationTab implements FuTab {


    private JPanel rootPanel;


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("前置操作", FuDocIcons.FU_REQUEST_HEADER, this.rootPanel).builder();
    }
}
