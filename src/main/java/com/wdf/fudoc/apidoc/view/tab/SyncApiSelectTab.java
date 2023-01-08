package com.wdf.fudoc.apidoc.view.tab;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComboBox;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiTableData;
import com.wdf.fudoc.apidoc.sync.strategy.SyncFuDocStrategy;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;

import javax.swing.*;
import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-01-08 20:58:46
 */
public class SyncApiSelectTab {

    private Project project;
    private JPanel rootPanel;
    private JPanel mainPanel;
    private JPanel leftPanel;
    private JPanel topPanel;
    private JPanel centerPanel;
    private JComboBox<String> projectNameComboBox;
    private JLabel projectNameLabel;
    private JPanel toolBarPanel;

    public FuTableComponent<SyncApiTableData> fuTableComponent = FuTableComponent.create(FuTableColumnFactory.syncApi(), SyncApiTableData.class);

    public SyncApiSelectTab(Project project) {
        this.project = project;

    }

    private void createUIComponents() {
//        //填充项目名称下拉框
//        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
//        BaseSyncConfigData enableConfigData = settingData.getEnableConfigData();
//        List<String> projectNameList = enableConfigData.getProjectNameList();
//        projectNameComboBox = new ComboBox<>(projectNameList.toArray(new String[0]));
//        //渲染左侧目录树
//        SyncFuDocStrategy service = ServiceHelper.getService(SyncFuDocStrategy.class);
//
//        //渲染右侧接口列表
//        this.centerPanel = fuTableComponent.createPanel();
    }
}
