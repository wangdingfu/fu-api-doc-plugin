package com.wdf.fudoc.request.factory;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.util.ui.components.BorderLayoutPanel;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-09-04 21:26:23
 */
public class FuTabBuilder {

    private final JPanel rootPanel;

    private final JBTabsImpl tabs;

    public FuTabBuilder() {
        this.rootPanel = new BorderLayoutPanel();
        this.tabs = new JBTabsImpl(null, null, ApplicationManager.getApplication());
    }

    public static FuTabBuilder getInstance() {
        return new FuTabBuilder();
    }


    public FuTabBuilder addTab(TabInfo tabInfo) {
        this.tabs.addTab(tabInfo);
        return this;
    }

    public JPanel build() {
        this.rootPanel.add(tabs.getComponent());
        return this.rootPanel;
    }

}
