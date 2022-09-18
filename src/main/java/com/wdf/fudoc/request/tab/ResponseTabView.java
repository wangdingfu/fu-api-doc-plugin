package com.wdf.fudoc.request.tab;

import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.request.InitRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import com.wdf.fudoc.util.FuComponentsUtils;

import javax.swing.*;

/**
 * http响应部分内容
 *
 * @author wangdingfu
 * @date 2022-09-17 18:05:45
 */
public class ResponseTabView implements FuTab, InitRequestData {

    private final Project project;

    private final JPanel rootPanel;

    public ResponseTabView(Project project) {
        this.project = project;
        this.rootPanel = FuComponentsUtils.createEmptyEditor();
        initUI();
    }


    private void initUI() {
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Response", null, this.rootPanel).builder();
    }


    /**
     * 初始化响应数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {

    }
}
