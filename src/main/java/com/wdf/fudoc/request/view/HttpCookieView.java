package com.wdf.fudoc.request.view;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.DialogWrapper;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.po.FuCookiePO;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2023-06-27 11:38:12
 */
public class HttpCookieView extends DialogWrapper {

    /**
     * table组件
     */
    private final FuTableComponent<FuCookiePO> fuTableComponent;

    private final FuRequestConfigStorage storage;

    public HttpCookieView(@Nullable Project project) {
        super(project, false);
        this.storage = FuRequestConfigStorageFactory.get(project);
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.cookie(), Lists.newArrayList(), FuCookiePO.class);
        setModal(true);
        init();
        setTitle("Cookie管理");
    }

    @Override
    protected @Nullable JComponent createCenterPanel() {
        return this.fuTableComponent.createPanel();
    }

    public void initData() {
        this.fuTableComponent.setDataList(storage.readData().getCookies());
    }

    @Override
    protected void doOKAction() {
        FuRequestConfigPO fuRequestConfigPO = storage.readData();
        fuRequestConfigPO.setCookies(this.fuTableComponent.getDataList());
        storage.saveData(fuRequestConfigPO);
        super.doOKAction();
    }
}
