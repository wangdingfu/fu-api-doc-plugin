package com.wdf.fudoc.request.view;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.request.data.FuRequestSettingData;
import com.wdf.fudoc.request.state.FuRequestSettingState;
import com.wdf.fudoc.request.tab.settings.GlobalConfigTab;
import com.wdf.fudoc.request.tab.settings.GlobalHeaderTab;
import com.wdf.fudoc.request.tab.settings.GlobalVariableTab;
import lombok.Getter;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;

/**
 * 【Fu Request】设置面板
 * 1、全局变量维护
 * 2、全局配置维护
 * 3、公共请求头维护
 *
 * @author wangdingfu
 * @date 2022-12-07 21:08:51
 */
public class FuRequestSettingView extends DialogWrapper {

    /**
     * 根面板
     */
    @Getter
    private final JPanel rootPanel;

    /**
     * tab页构建器
     */
    private final FuTabBuilder fuTabBuilder = FuTabBuilder.getInstance();


    private GlobalConfigTab globalConfigTab;
    private GlobalVariableTab globalVariableTab;
    private GlobalHeaderTab globalHeaderTab;


    public FuRequestSettingView(@Nullable Project project) {
        super(project, true);
        this.rootPanel = new JPanel(new BorderLayout());
        setTitle("【Fu Request】设置");
        initSettingPanel();
        init();
    }


    /**
     * 初始化设置面板
     */
    private void initSettingPanel() {
        this.globalConfigTab = new GlobalConfigTab();
        this.globalVariableTab = new GlobalVariableTab();
        this.globalHeaderTab = new GlobalHeaderTab();
        //添加tab页
        fuTabBuilder.addTab(this.globalHeaderTab).addTab(this.globalConfigTab).addTab(this.globalVariableTab);
        this.rootPanel.add(fuTabBuilder.build(), BorderLayout.CENTER);
        initData();
    }


    /**
     * 初始化数据
     */
    public void initData() {
        FuRequestSettingData data = FuRequestSettingState.getData();
        globalHeaderTab.initData(data.getCommonHeaderList());
    }


    /**
     * 保存数据
     */
    public void apply() {
        FuRequestSettingData data = FuRequestSettingState.getData();
        data.setCommonHeaderList(globalHeaderTab.getData());
    }


    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.rootPanel;
    }
}
