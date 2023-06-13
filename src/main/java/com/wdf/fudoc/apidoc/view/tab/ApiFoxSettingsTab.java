package com.wdf.fudoc.apidoc.view.tab;

import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.listener.FuViewListener;
import icons.FuDocIcons;

import javax.swing.*;
import java.awt.*;

/**
 * apiFox同步文档设置
 * @author wangdingfu
 * @date 2023-05-12 15:32:30
 */
public class ApiFoxSettingsTab implements FuTab, FuViewListener {

    private final JPanel rootPanel;


    public ApiFoxSettingsTab() {
        this.rootPanel = new JPanel(new BorderLayout());
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("apiFox", FuDocIcons.FU_API_FOX, this.rootPanel).builder();
    }

    @Override
    public void apply() {

    }

    @Override
    public void reset() {

    }
}
