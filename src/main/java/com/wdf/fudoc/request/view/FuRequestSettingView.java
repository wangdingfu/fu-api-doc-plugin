package com.wdf.fudoc.request.view;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.request.constants.enumtype.ScriptType;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.tab.settings.*;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
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

    private final Project project;

    private GlobalConfigTab globalConfigTab;
    private GlobalVariableTab globalVariableTab;
    private GlobalHeaderTab globalHeaderTab;
    private GlobalCookiesTab globalCookiesTab;
    private GlobalScriptTab globalPreScriptTab;
//    private GlobalScriptTab globalPostScriptTab;


    public FuRequestSettingView(@Nullable Project project) {
        super(project, true);
        this.project = project;
        this.rootPanel = new JPanel(new BorderLayout());
        setTitle(FuBundle.message("fudoc.settings.title"));
        initPanel();
        setModal(false);
        init();
    }


    /**
     * 初始化设置面板
     */
    private void initPanel() {
        this.globalConfigTab = new GlobalConfigTab();
        this.globalVariableTab = new GlobalVariableTab(project, getDisposable());
        this.globalHeaderTab = new GlobalHeaderTab(project, getDisposable());
        this.globalCookiesTab = new GlobalCookiesTab();
        this.globalPreScriptTab = new GlobalScriptTab(project, ScriptType.PRE_SCRIPT, getDisposable());
//        this.globalPostScriptTab = new GlobalScriptTab(project, ScriptType.POST_SCRIPT, getDisposable());
        //初始化数据
        initData();
        //添加tab页
        fuTabBuilder
                //全局请求头
                .addTab(this.globalHeaderTab)
                //请求配置
                .addTab(this.globalConfigTab)
                //cookies
                .addTab(this.globalCookiesTab)
                //全局变量
                .addTab(this.globalVariableTab)
                //添加前置脚本
                .addTab(this.globalPreScriptTab);
        this.rootPanel.add(fuTabBuilder.build(), BorderLayout.CENTER);
    }

    public void select(String tab) {
        ApplicationManager.getApplication().invokeLater(() -> fuTabBuilder.select(tab));
    }

    /**
     * 初始化数据
     */
    public void initData() {
        FuRequestConfigPO configPO = FuRequestConfigStorage.get(project).readData();
        //初始化全局请求头
        this.globalHeaderTab.initData(configPO);
        //初始化配置
        this.globalConfigTab.initData(configPO);
        //初始化cookies
        this.globalCookiesTab.initData(configPO);
        //初始化全局变量
        this.globalVariableTab.initData(configPO);
        //初始化全局前置脚本
        this.globalPreScriptTab.initData(configPO);
        //初始化全局后置脚本
//        this.globalPostScriptTab.initData(configPO);
    }


    /**
     * 点击ok按钮时触发保存
     */
    @Override
    protected void doOKAction() {
        this.apply();
        super.doOKAction();
    }

    public void apply() {
        FuRequestConfigStorage storage = FuRequestConfigStorage.get(project);
        FuRequestConfigPO configPO = storage.readData();
        this.globalHeaderTab.saveData(configPO);
        this.globalConfigTab.saveData(configPO);
        this.globalCookiesTab.saveData(configPO);
        this.globalVariableTab.saveData(configPO);
        this.globalPreScriptTab.saveData(configPO);
//        this.globalPostScriptTab.saveData(configPO);?
        storage.saveData(configPO);
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.rootPanel;
    }
}
