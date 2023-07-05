package com.wdf.fudoc.apidoc.view.tab;

import com.google.common.collect.Lists;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.ui.JBPopupMenu;
import com.intellij.openapi.ui.messages.MessageDialog;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.components.ActionLink;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncProjectSetting;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.data.SyncApiConfigData;
import com.wdf.fudoc.apidoc.sync.data.*;
import com.wdf.fudoc.common.FuBundle;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.common.enumtype.FuColor;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.PlaceholderTextField;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuViewListener;
import com.wdf.fudoc.util.PopupUtils;
import icons.FuDocIcons;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-05 18:46:36
 */
public class ShowDocSettingTab implements FuTab, FuViewListener {
    private JRootPane rootPane;
    private JPanel rootPanel;
    private JPanel basicPanel;
    private JTextField domainField;
    private JCheckBox enableBox;
    private JTextField usernameField;
    private JTextField passwordField;
    private JLabel domainLabel;
    private JLabel usernameLabel;
    private JLabel passwordLabel;
    private JButton loginBtn;
    private JPanel mainPanel;

    private final FuTableComponent<ShowDocProjectTableData> projectTable;

    public ShowDocSettingTab() {
        this.basicPanel.setBorder(JBUI.Borders.emptyTop(10));
        this.mainPanel.setBorder(JBUI.Borders.emptyTop(10));
        this.projectTable = FuTableComponent.create(FuTableColumnFactory.showDoc(), ShowDocProjectTableData.class);
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


    @Override
    public TabInfo getTabInfo() {
        JPanel slidePanel = new JPanel(new BorderLayout());
        //登录showDoc，进入具体项目后，点击项目设置-“开放API”便可看到
        ActionLink actionLink = new ActionLink(FuBundle.message("fudoc.sync.showdoc.token.link"), e -> {
        });
        actionLink.setForeground(FuColor.color6.color());
        slidePanel.add(actionLink, BorderLayout.EAST);
        return FuTabComponent.getInstance("ShowDoc", FuDocIcons.FU_API_SHOW_DOC, this.rootPane).builder(slidePanel);
    }

    private void initEnableBox() {
        this.enableBox.addItemListener(e -> {
            FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
            if (this.enableBox.isSelected()) {
                //如果开启了就设置启用的为showDoc 否则不设置（都没有设置情况会有默认值）
                settingData.setEnable(ApiDocSystem.SHOW_DOC.getCode());
            } else {
                settingData.setEnable(settingData.getDefault());
            }
        });
    }


    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        rootPane.setDefaultButton(this.loginBtn);
        this.enableBox.setSelected(ApiDocSystem.SHOW_DOC.getCode().equals(FuDocSyncSetting.getSettingData().getEnable()));
    }


    @Override
    public void apply() {
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        if (this.enableBox.isSelected()) {
            //如果开启了就设置启用的为showDoc 否则不设置（都没有设置情况会有默认值）
            settingData.setEnable(ApiDocSystem.SHOW_DOC.getCode());
        }
        ShowDocConfigData showDoc = settingData.getShowDoc();
        showDoc.setBaseUrl(this.domainField.getText());
        //获取当前需要保存的项目配置
        FuDocSyncProjectSetting instance = FuDocSyncProjectSetting.getInstance();
        SyncApiConfigData state;
        if (Objects.nonNull(instance) && Objects.nonNull(state = instance.getState())) {
            state.setShowDocConfigList(this.projectTable.getDataList());
            instance.loadState(state);
        }
    }

    @Override
    public void reset() {
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        this.enableBox.setSelected(ApiDocSystem.SHOW_DOC.getCode().equals(settingData.getEnable()));
        ShowDocConfigData showDoc = settingData.getShowDoc();
        this.domainField.setText(showDoc.getBaseUrl());
        //获取当前需要保存的项目配置
        FuDocSyncProjectSetting instance = FuDocSyncProjectSetting.getInstance();
        SyncApiConfigData state;
        if (Objects.nonNull(instance) && Objects.nonNull(state = instance.getState())) {
            this.projectTable.setDataList(Lists.newArrayList(state.getShowDocConfigList()));
        }
    }

    private void createUIComponents() {
        this.domainField = new PlaceholderTextField("请输入你的ShowDoc服务地址，例如:http://showdoc.fudoc.cn [不输入则默认ShowDoc官网地址]");
    }
}
