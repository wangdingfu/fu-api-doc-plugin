package com.wdf.fudoc.request.tab.settings;

import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuDataTab;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import icons.FuDocIcons;

import javax.swing.*;
import java.awt.*;

/**
 * 全局变量维护
 *
 * @author wangdingfu
 * @date 2022-12-07 21:44:12
 */
public class GlobalVariableTab implements FuDataTab<FuRequestConfigPO> {


    private final JPanel rootPanel;

    public GlobalVariableTab() {
        this.rootPanel = new JPanel(new BorderLayout());
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("全局变量", FuDocIcons.FU_REQUEST_PARAMS, this.rootPanel).builder();
    }

    @Override
    public void initData(FuRequestConfigPO data) {

    }

    @Override
    public void saveData(FuRequestConfigPO data) {

    }
}
