package com.wdf.fudoc.test.factory;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.TabsListener;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.util.ui.JBFont;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.wdf.fudoc.common.FuTab;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2022-09-04 21:26:23
 */
public class FuTabBuilder {

    private final JPanel rootPanel;

    @Getter
    private final JBTabsImpl tabs;

    private final Map<String, FuTab> fuTabMap = new ConcurrentHashMap<>();

    private final Map<String, TabInfo> tabInfoMap = new ConcurrentHashMap<>();

    public FuTabBuilder() {
        this.rootPanel = new BorderLayoutPanel();
        this.tabs = new JBTabsImpl(null, null, ApplicationManager.getApplication());
    }

    public static FuTabBuilder getInstance() {
        return new FuTabBuilder();
    }


    public FuTabBuilder addTab(TabInfo tabInfo) {
        this.tabs.addTab(tabInfo);
        tabInfoMap.put(tabInfo.getText(), tabInfo);
        return this;
    }

    public FuTabBuilder addTab(FuTab fuTab) {
        TabInfo tabInfo = fuTab.getTabInfo();
        fuTabMap.put(tabInfo.getText(), fuTab);
        return addTab(tabInfo);
    }

    public JPanel build() {
        addListener();
        this.rootPanel.add(tabs.getComponent());
        this.rootPanel.setFont(JBUI.Fonts.label(11));
        return this.rootPanel;
    }

    /**
     * 选中指定tab
     *
     * @param text tab标题
     */
    public void select(String text) {
        TabInfo tabInfo = tabInfoMap.get(text);
        if (Objects.nonNull(tabInfo)) {
            tabs.select(tabInfo, false);
        }
    }

    public void addListener() {
        this.tabs.addListener(new TabsListener() {
            @Override
            public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
                String text = newSelection.getText();
                if (StringUtils.isNotBlank(text)) {
                    FuTab fuTab = fuTabMap.get(text);
                    if (Objects.nonNull(fuTab)) {
                        fuTab.selectionChanged(oldSelection, newSelection);
                    }
                }
            }
        });
    }

}
