package com.wdf.fudoc.components.factory;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.TabsListener;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.wdf.fudoc.common.FuTab;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.awt.*;
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
        return addTab(fuTab, null);
    }

    public FuTabBuilder addTab(FuTab fuTab, JPanel sidePanel) {
        TabInfo tabInfo = fuTab.getTabInfo();
        addSideComponent(tabInfo, sidePanel);
        fuTabMap.put(tabInfo.getText(), fuTab);
        return addTab(tabInfo);
    }

    public JPanel build() {
        addListener();
        this.rootPanel.add(tabs.getComponent());
        this.rootPanel.setFont(JBUI.Fonts.label(11));
        return this.rootPanel;
    }

    public void addSideComponent(JPanel sideComponent) {
        this.tabInfoMap.forEach((key, value) -> addSideComponent(value, sideComponent));
    }

    public void addSideComponent(String title, JPanel sideComponent) {
        addSideComponent(this.tabInfoMap.get(title), sideComponent);
    }

    private void addSideComponent(TabInfo tabInfo, JPanel sideComponent) {
        if (Objects.nonNull(tabInfo) && Objects.nonNull(sideComponent)) {
            JComponent beforeSide = tabInfo.getSideComponent();
            if (Objects.isNull(beforeSide)) {
                JPanel sidePanel = new JPanel(new BorderLayout());
                sidePanel.add(sideComponent, BorderLayout.EAST);
                tabInfo.setSideComponent(sideComponent);
            } else {
                beforeSide.add(sideComponent, BorderLayout.EAST);
                tabInfo.setSideComponent(beforeSide);
            }
        }
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
