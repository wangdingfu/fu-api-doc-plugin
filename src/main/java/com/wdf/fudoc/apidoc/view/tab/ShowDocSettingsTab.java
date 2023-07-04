package com.wdf.fudoc.apidoc.view.tab;

import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.listener.FuViewListener;
import icons.FuDocIcons;

import javax.swing.*;
import java.awt.*;

/**
 * ShowDoc配置页面
 *
 * @author wangdingfu
 * @date 2023-01-06 23:11:47
 */
public class ShowDocSettingsTab implements FuTab, FuViewListener {

    /**
     * 根面板
     */
    private JPanel rootPanel;

    public ShowDocSettingsTab() {
        this.rootPanel = new JPanel(new BorderLayout());
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("ShowDoc", FuDocIcons.FU_API_SHOW_DOC, this.rootPanel).builder();
    }

    @Override
    public void apply() {

    }

    @Override
    public void reset() {

    }

}
