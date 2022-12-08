package com.wdf.fudoc.request.tab.settings;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.tab.AbstractBulkEditTabLinkage;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import icons.FuDocIcons;

/**
 * 全局请求头维护
 *
 * @author wangdingfu
 * @date 2022-12-07 21:47:14
 */
public class GlobalHeaderTab extends AbstractBulkEditTabLinkage implements FuTab {

    /**
     * table组件
     */
    private final FuTableComponent<KeyValueTableBO> fuTableComponent;
    /**
     * 批量编辑请求参数组件
     */
    private final FuEditorComponent fuEditorComponent;


    public GlobalHeaderTab() {
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class);
        //文本编辑器
        this.fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "");
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("全局Header", FuDocIcons.FU_REQUEST_HEADER, fuTableComponent.createPanel()).addBulkEditBar(fuEditorComponent.getMainPanel(), this).builder();
    }

    @Override
    protected FuTableComponent<KeyValueTableBO> getTableComponent(String title) {
        return this.fuTableComponent;
    }

    @Override
    protected FuEditorComponent getEditorComponent(String title) {
        return this.fuEditorComponent;
    }
}
