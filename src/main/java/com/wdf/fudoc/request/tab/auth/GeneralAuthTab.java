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




    /**
     * 添加一条配置数据时调用
     * @param data 配置数据
     */
    @Override
    public void doAction(AuthConfigData data) {


    }

    /**
     * 离开该配置时调用 需要获取配置的数据并保存
     * @param data 配置数据
     */
    @Override
    public void doActionAfter(AuthConfigData data) {

    }

    @Override
    public void dispose() {

    }
}
