package com.wdf.fudoc.apidoc.view.tab;

import com.google.common.collect.Lists;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncProjectSetting;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.data.SyncApiConfigData;
import com.wdf.fudoc.apidoc.sync.data.ApiFoxConfigData;
import com.wdf.fudoc.apidoc.sync.data.ApiFoxProjectTableData;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.PlaceholderTextField;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuViewListener;
import icons.FuDocIcons;

import javax.swing.*;
import java.awt.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-01 19:52:14
 */
public class ApiFoxSettingTab implements FuTab, FuViewListener {

    private JRootPane rootPane;
    private JPanel rootPanel;
    private JPanel basicPanel;
    private JTextField domainField;
    private JCheckBox enableBox;
    private JTextField authTokenField;
    private JButton loginBtn;
    private JLabel domainLabel;
    private JLabel tokenLabel;
    private JPanel mainPanel;
    private final FuTableComponent<ApiFoxProjectTableData> projectTable;

    public ApiFoxSettingTab() {
        this.basicPanel.setBorder(JBUI.Borders.emptyTop(10));
        this.mainPanel.setBorder(JBUI.Borders.emptyTop(10));
        this.projectTable = FuTableComponent.create(FuTableColumnFactory.apiFox(), ApiFoxProjectTableData.class);
        this.mainPanel.add(this.projectTable.createPanel(), BorderLayout.CENTER);
        initEnableBox();
        initRootPane();
    }


    public void initRootPane() {
        this.rootPane = new JRootPane();
        final IdeGlassPaneImpl glass = new IdeGlassPaneImpl(rootPane);
        rootPane.setGlassPane(glass);
        glass.setVisible(true);
        rootPane.setContentPane(this.rootPanel);
        rootPane.setDefaultButton(this.loginBtn);
    }

    private void createUIComponents() {
        this.domainField = new PlaceholderTextField(FuBundle.message("fudoc.sync.apifox.domain", UrlConstants.API_FOX));
        this.authTokenField = new PlaceholderTextField(FuBundle.message("fudoc.sync.apifox.token"));
    }

    @Override
    public TabInfo getTabInfo() {
        JPanel slidePanel = new JPanel(new BorderLayout());
        ActionLink actionLink = new ActionLink(FuBundle.message("fudoc.sync.apifox.token.link"), e -> {
            BrowserUtil.browse("https://www.apifox.cn/help/openapi/");
        });
        actionLink.setForeground(FuColor.color6.color());
        slidePanel.add(actionLink, BorderLayout.EAST);
        return FuTabComponent.getInstance("apiFox", FuDocIcons.FU_API_FOX, this.rootPane).builder(slidePanel);
    }


    private void initEnableBox() {
        this.enableBox.addItemListener(e -> {
            FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
            if (this.enableBox.isSelected()) {
                //如果开启了就设置启用的为apifox 否则不设置（都没有设置情况会有默认值）
                settingData.setEnable(ApiDocSystem.API_FOX.getCode());
            } else {
                settingData.setEnable(settingData.getDefault());
            }
        });
    }


    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        rootPane.setDefaultButton(this.loginBtn);
        this.enableBox.setSelected(ApiDocSystem.API_FOX.getCode().equals(FuDocSyncSetting.getSettingData().getEnable()));
    }


    @Override
    public void apply() {
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        ApiFoxConfigData apiFox = settingData.getApiFox();
        apiFox.setBaseUrl(this.domainField.getText());
        apiFox.setToken(this.authTokenField.getText());
        //获取当前需要保存的项目配置
        FuDocSyncProjectSetting instance = FuDocSyncProjectSetting.getInstance();
        SyncApiConfigData state;
        if (Objects.nonNull(instance) && Objects.nonNull(state = instance.getState())) {
            state.setApiFoxConfigList(this.projectTable.getDataList());
            instance.loadState(state);
        }
    }

    @Override
    public void reset() {
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        this.enableBox.setSelected(ApiDocSystem.API_FOX.getCode().equals(settingData.getEnable()));
        ApiFoxConfigData apiFox = settingData.getApiFox();
        this.domainField.setText(apiFox.getBaseUrl());
        this.authTokenField.setText(apiFox.getToken());
        //获取当前需要保存的项目配置
        FuDocSyncProjectSetting instance = FuDocSyncProjectSetting.getInstance();
        SyncApiConfigData state;
        if (Objects.nonNull(instance) && Objects.nonNull(state = instance.getState())) {
            this.projectTable.setDataList(Lists.newArrayList(state.getApiFoxConfigList()));
        }
    }
}
