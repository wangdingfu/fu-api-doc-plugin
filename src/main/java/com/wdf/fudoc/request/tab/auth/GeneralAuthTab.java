package com.wdf.fudoc.request.tab.auth;

import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.listener.FuActionListener;
import com.wdf.fudoc.request.pojo.AuthConfigData;
import com.wdf.fudoc.request.view.GeneralAuthView;

/**
 * 普通授权tab
 *
 * @author wangdingfu
 * @date 2023-05-02 18:50:53
 */
public class GeneralAuthTab implements FuTab, FuActionListener<AuthConfigData> {
    private final static String TAB = "General";

    private final GeneralAuthView generalAuthView;

    public GeneralAuthTab(Project project) {
        this.generalAuthView = new GeneralAuthView();
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(TAB, null, this.generalAuthView.getRootPanel()).builder();
    }


    @Override
    public void doAction(AuthConfigData data) {

    }

    @Override
    public void doActionAfter(AuthConfigData data) {

    }
}
