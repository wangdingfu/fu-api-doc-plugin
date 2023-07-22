package com.wdf.fudoc.request.view;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.po.GlobalPreScriptPO;
import com.wdf.fudoc.request.tab.settings.*;
import com.wdf.fudoc.spring.SpringBootEnvLoader;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import lombok.Getter;
import org.apache.commons.collections.CollectionUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
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
    private GlobalCookiesTab globalCookiesTab;


    private final AtomicInteger preScriptIndex = new AtomicInteger(0);

    private final List<GlobalPreScriptTab> preScriptTabs = Lists.newArrayList();


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
                .addTab(this.globalVariableTab);
        //添加前置脚本tab
        this.preScriptTabs.forEach(fuTabBuilder::addTab);
        this.rootPanel.add(fuTabBuilder.build(), BorderLayout.CENTER);
    }

    public void select(String tab){
        ApplicationManager.getApplication().invokeLater(()->fuTabBuilder.select(tab));
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
        Map<String, GlobalPreScriptPO> preScriptMap = configPO.getPreScriptMap();
        //如果不存在默认前置脚本数据 则需要添加上默认的前置脚本数据
        Set<String> applicationList = SpringBootEnvLoader.getApplication(project);
        if(CollectionUtils.isNotEmpty(applicationList)){

        }
        GlobalPreScriptPO globalPreScriptPO = preScriptMap.get(GlobalPreScriptTab.TITLE);
        if (Objects.isNull(globalPreScriptPO)) {
            globalPreScriptPO = new GlobalPreScriptPO();
            preScriptMap.put(GlobalPreScriptTab.TITLE, globalPreScriptPO);
        }
        //对前置脚本排序(为了展示顺序性) 创建前置脚本tab 并 初始化数据
        Lists.newArrayList(preScriptMap.keySet()).stream().sorted().forEach(f -> this.preScriptTabs.add(new GlobalPreScriptTab(project, f, configPO, getDisposable())));

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
        //持久化配置数据
        this.preScriptTabs.forEach(f -> f.saveData(configPO));
        this.globalHeaderTab.saveData(configPO);
        this.globalConfigTab.saveData(configPO);
        this.globalCookiesTab.saveData(configPO);
        this.globalVariableTab.saveData(configPO);
        storage.saveData(configPO);
    }



    protected class CreateTabAction extends DialogWrapperAction {
        protected CreateTabAction(String title) {
            super(title);
            putValue(Action.SMALL_ICON, AllIcons.General.Add);
        }

        @Override
        protected void doAction(ActionEvent e) {
            //新增前置脚本
            GlobalPreScriptTab globalPreScriptTab = new GlobalPreScriptTab(project, GlobalPreScriptTab.TITLE + preScriptIndex.incrementAndGet(), null, getDisposable());
            preScriptTabs.add(globalPreScriptTab);
            fuTabBuilder.addTab(globalPreScriptTab);
        }
    }

    protected class RemoveTabAction extends DialogWrapperAction {
        protected RemoveTabAction(String title) {
            super(title);
            putValue(Action.SMALL_ICON, AllIcons.General.Remove);
        }

        @Override
        protected void doAction(ActionEvent e) {
//            //新增前置脚本
//            FuTab selected = fuTabBuilder.getSelected();
//            if (Objects.nonNull(selected)) {
//                String text = selected.getTabInfo().getText();
//                if (GlobalPreScriptTab.TITLE.equals(text)) {
//                    return;
//                }
//                boolean isDelete = preScriptTabs.removeIf(f -> f.getTabInfo().getText().equals(text));
//                if (isDelete) {
//                    fuTabBuilder.removeTab(text);
//                    Map<String, GlobalPreScriptPO> preScriptMap = configPO.getPreScriptMap();
//                    preScriptMap.remove(text);
//                }
//            }
        }
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.rootPanel;
    }
}
