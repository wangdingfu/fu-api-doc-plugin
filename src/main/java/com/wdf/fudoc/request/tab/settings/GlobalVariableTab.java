package com.wdf.fudoc.request.tab.settings;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuDataTab;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.TabActionBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.po.GlobalKeyValuePO;
import com.wdf.fudoc.request.tab.AbstractBulkEditTabLinkage;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
import icons.FuDocIcons;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * 全局变量维护
 *
 * @author wangdingfu
 * @date 2022-12-07 21:44:12
 */
public class GlobalVariableTab extends AbstractBulkEditTabLinkage<GlobalKeyValuePO> implements FuDataTab<FuRequestConfigPO> {


    /**
     * table组件
     */
    private final FuTableComponent<GlobalKeyValuePO> fuTableComponent;
    /**
     * 批量编辑请求参数组件
     */
    private final FuEditorComponent fuEditorComponent;

    private FuTabComponent fuTabComponent;

    private final Project project;

    private static final String TITLE = "全局变量";


    public GlobalVariableTab(Project project) {
        this.project = project;
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.globalConfig("变量名称", "变量值"), Lists.newArrayList(), GlobalKeyValuePO.class);
        //文本编辑器
        this.fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "",this);
    }

    @Override
    public TabInfo getTabInfo() {
        this.fuTabComponent = FuTabComponent.getInstance(TITLE, FuDocIcons.FU_VARIABLE, fuTableComponent.createPanel()).addBulkEditBar(fuEditorComponent.getMainPanel(), this);
        return this.fuTabComponent.builder();
    }

    @Override
    protected FuTableComponent<GlobalKeyValuePO> getTableComponent(String title) {
        return this.fuTableComponent;
    }

    @Override
    protected FuEditorComponent getEditorComponent(String title) {
        return this.fuEditorComponent;
    }


    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        initData(FuRequestConfigStorageFactory.get(project).readData());
    }


    @Override
    public void moveOff() {
        //离开当前tab时 保存数据
        FuRequestConfigPO fuRequestConfigPO = FuRequestConfigStorageFactory.get(project).readData();
        fuRequestConfigPO.setGlobalVariableList(Lists.newArrayList(this.fuTableComponent.getDataList()));
    }

    @Override
    public void initData(FuRequestConfigPO configPO) {
        fuTableComponent.setDataList(Lists.newArrayList(configPO.getGlobalVariableList()));
    }


    @Override
    public void saveData(FuRequestConfigPO configPO) {
        TabActionBO tabActionBO = this.fuTabComponent.getDefaultAction();
        if (Objects.nonNull(tabActionBO) && tabActionBO.isSelect()) {
            //如果当前是编辑器状态 则需要从编辑器组件同步数据到table组件
            bulkEditToTableData(TITLE);
        }
        configPO.setGlobalVariableList(Lists.newArrayList(fuTableComponent.getDataList()));
    }

    @Override
    public void dispose() {

    }
}
