package com.wdf.fudoc.request.tab.settings;

import com.google.common.collect.Lists;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuDataTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.TabActionBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.po.GlobalKeyValuePO;
import com.wdf.fudoc.request.tab.AbstractBulkEditTabLinkage;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import icons.FuDocIcons;

import java.util.Objects;

/**
 * 全局请求头维护
 *
 * @author wangdingfu
 * @date 2022-12-07 21:47:14
 */
public class GlobalHeaderTab extends AbstractBulkEditTabLinkage<GlobalKeyValuePO> implements FuDataTab<FuRequestConfigPO> {

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


    private static final String TITLE = "公共请求头";


    public GlobalHeaderTab(Project project, Disposable disposable) {
        this.project = project;
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.globalConfig("请求头名称", "请求头值"), Lists.newArrayList(), GlobalKeyValuePO.class);
        //文本编辑器
        this.fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "",disposable);
    }

    @Override
    public TabInfo getTabInfo() {
        this.fuTabComponent = FuTabComponent.getInstance(TITLE, FuDocIcons.FU_REQUEST_HEADER, fuTableComponent.createPanel()).addBulkEditBar(fuEditorComponent.getMainPanel(), this);
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
        initData(FuRequestConfigStorage.get(project).readData());
    }

    @Override
    public void moveOff() {
        //离开当前tab时 保存数据
        FuRequestConfigPO fuRequestConfigPO = FuRequestConfigStorage.get(project).readData();
        fuRequestConfigPO.setGlobalHeaderList(this.fuTableComponent.getDataList());
    }

    @Override
    public void initData(FuRequestConfigPO configPO) {
        fuTableComponent.setDataList(Lists.newArrayList(configPO.getGlobalHeaderList()));
    }


    @Override
    public void saveData(FuRequestConfigPO configPO) {
        TabActionBO tabActionBO = this.fuTabComponent.getDefaultAction();
        if (Objects.nonNull(tabActionBO) && tabActionBO.isSelect()) {
            //如果当前是编辑器状态 则需要从编辑器组件同步数据到table组件
            bulkEditToTableData(TITLE);
        }
        configPO.setGlobalHeaderList(fuTableComponent.getDataList());
    }

}
