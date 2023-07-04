package com.wdf.fudoc.request.tab.settings;

import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import icons.FuDocIcons;

import javax.swing.*;
import java.awt.*;

/**
 * 全局配置维护
 *
 * @author wangdingfu
 * @date 2022-12-07 21:48:14
 */
public class GlobalConfigTab implements FuTab {

    private final JPanel rootPanel;

    public GlobalConfigTab() {
        this.rootPanel = new JPanel(new BorderLayout());
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("全局配置", FuDocIcons.FU_SETTINGS, this.rootPanel).builder();
    }

}
