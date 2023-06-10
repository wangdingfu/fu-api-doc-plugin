package com.wdf.fudoc.request.view;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.request.data.FuRequestSettingData;
import com.wdf.fudoc.request.state.FuRequestSettingState;
import com.wdf.fudoc.request.tab.settings.GlobalConfigTab;
import com.wdf.fudoc.request.tab.settings.GlobalHeaderTab;
import com.wdf.fudoc.request.tab.settings.GlobalPreScriptTab;
import com.wdf.fudoc.request.tab.settings.GlobalVariableTab;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

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

    private volatile AtomicInteger preScriptIndex = new AtomicInteger(0);

    private final List<GlobalPreScriptTab> preScriptTabs = Lists.newArrayList();


    public FuRequestSettingView(@Nullable Project project) {
        super(project, true);
        this.project = project;
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
        //默认前置脚本
        this.preScriptTabs.add(new GlobalPreScriptTab(project));
        //添加tab页
        fuTabBuilder
                //全局请求头
                .addTab(this.globalHeaderTab)
                //请求配置
                .addTab(this.globalConfigTab)
                //全局变量
                .addTab(this.globalVariableTab);

        //前置脚本
        this.preScriptTabs.forEach(fuTabBuilder::addTab);
        this.rootPanel.add(fuTabBuilder.build(), BorderLayout.CENTER);
        initData();
    }


    /**
     * 初始化数据
     */
    public void initData() {
        FuRequestSettingData data = FuRequestSettingState.getData();
        this.globalHeaderTab.initData(data.getCommonHeaderList());
        this.preScriptTabs.forEach(f -> f.initData(null));
        this.globalVariableTab.initData(null);
    }


    @Override
    protected void doOKAction() {
        apply();
        super.doOKAction();
    }

    @Override
    protected Action @NotNull [] createActions() {
        List<Action> actionList = Lists.newArrayList(super.createActions());
        actionList.add(new CreateTabAction("新增前置脚本"));
        return actionList.toArray(new Action[]{});
    }


    protected class CreateTabAction extends DialogWrapperAction {
        protected CreateTabAction(String title) {
            super(title);
            putValue(Action.SMALL_ICON, AllIcons.General.Add);
        }

        @Override
        protected void doAction(ActionEvent e) {
            //新增前置脚本
            GlobalPreScriptTab globalPreScriptTab = new GlobalPreScriptTab(project, GlobalPreScriptTab.TITLE + preScriptIndex.incrementAndGet());
            preScriptTabs.add(globalPreScriptTab);
            fuTabBuilder.addTab(globalPreScriptTab);
        }
    }

    /**
     * 保存数据
     */
    public void apply() {
        //持久化配置数据
        this.preScriptTabs.forEach(f -> f.saveData(null));
        this.globalVariableTab.saveData(null);
        FuRequestSettingData data = FuRequestSettingState.getData();
        data.setCommonHeaderList(globalHeaderTab.getData());
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.rootPanel;
    }
}
