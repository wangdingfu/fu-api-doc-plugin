package com.wdf.fudoc.apidoc.view.tab;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.Messages;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.YApiProjectTableData;
import com.wdf.fudoc.apidoc.sync.data.YapiConfigData;
import com.wdf.fudoc.apidoc.sync.dto.YApiProjectInfoDTO;
import com.wdf.fudoc.apidoc.sync.service.YApiService;
import com.wdf.fudoc.common.FuDocMessageBundle;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.common.constant.MessageConstants;
import com.wdf.fudoc.common.notification.FuDocNotification;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.PlaceholderTextField;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.components.listener.FuViewListener;
import com.wdf.fudoc.components.validator.InputExistsValidator;
import com.wdf.fudoc.util.ObjectUtils;
import com.wdf.fudoc.util.ProjectUtils;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.*;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-01-07 01:07:19
 */
public class YApiSettingTab implements FuTab, FuViewListener, FuTableListener<YApiProjectTableData> {
    private JRootPane rootPane;
    private JPanel rootPanel;
    private JPanel baseInfoPanel;
    private JPanel mainPanel;
    private JTextField userName;
    private JTextField yapiPwd;
    private JButton loginBtn;
    private JLabel userNameTitle;
    private JLabel passwordTitle;
    private JTextField baseUrl;
    private JCheckBox isEnable;
    private JLabel baseUrlTitle;
    private JPanel toolPanel;
    private JButton clearAllBtn;
    private JButton clearRecordBtn;

    private static final TitledBorder baseInfoBorder = IdeBorderFactory.createTitledBorder(FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_BASE_TITLE));
    private static final TitledBorder mainBorder = IdeBorderFactory.createTitledBorder(FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_MAIN_TITLE));

    private static final String SYNC_TOKEN = FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_TOKEN);
    private static final String SYNC_TOKEN_TITLE = FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_TOKEN_TITLE);
    // 当前项目配置存在其他项目在使用 确定要删除吗
    private static final String DELETE_CONFIG_TIP = FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_DELETE_PROJECT_CONFIG);
    // 确认删除项目配置
    private static final String CONFIRM_DELETE_TITLE = FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_DELETE_PROJECT_CONFIG_TITLE);


    /**
     * 项目配置
     */
    private final FuTableComponent<YApiProjectTableData> fuTableComponent;

    public YApiSettingTab() {
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.yapi(), Lists.newArrayList(), YApiProjectTableData.class);
        this.fuTableComponent.addListener(this);
        this.mainPanel.add(fuTableComponent.createPanel(), BorderLayout.CENTER);
        initRootPane();
        this.rootPanel.setBorder(JBUI.Borders.emptyTop(10));
        this.baseInfoPanel.setBorder(baseInfoBorder);
        this.mainPanel.setBorder(mainBorder);
        initBtn();
    }

    public void initBtn() {
        FuDocSyncSetting instance = FuDocSyncSetting.getInstance();
        if (Objects.isNull(instance)) {
            return;
        }
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        BaseSyncConfigData enableConfigData = settingData.getEnableConfigData();
        this.clearAllBtn.addActionListener(e -> {
            //清楚所有缓存数据
            enableConfigData.clear(true);
            instance.loadState(settingData);
        });
        this.clearRecordBtn.addActionListener(e -> {
            //清楚同步记录数据
            enableConfigData.clear(false);
            instance.loadState(settingData);
        });
    }


    public void initRootPane() {
        this.rootPane = new JRootPane();
        final IdeGlassPaneImpl glass = new IdeGlassPaneImpl(rootPane);
        rootPane.setGlassPane(glass);
        glass.setVisible(true);
        rootPane.setContentPane(this.rootPanel);
        rootPane.setDefaultButton(this.loginBtn);
    }


    /**
     * 校验项目配置的数据是否可以删除
     *
     * @param data 需要删除的数据
     * @return true 可以删除 false 不可以删除
     */
    @Override
    public boolean isCanDelete(YApiProjectTableData data) {
        List<String> projectKeyList = data.getProjectKeyList();
        String currentProjectPath = ProjectUtils.getCurrentProjectPath();
        if (CollectionUtils.isEmpty(projectKeyList) || (projectKeyList.size() == 1 && currentProjectPath.equals(projectKeyList.get(0)))) {
            return true;
        }
        return Messages.showYesNoDialog(DELETE_CONFIG_TIP, CONFIRM_DELETE_TITLE, Messages.getQuestionIcon()) == Messages.YES;
    }

    @Override
    public boolean customerAddData() {
        return true;
    }

    /**
     * 新增项目配置时会被调用 返回当前项目的配置
     */
    @Override
    public YApiProjectTableData addData() {
        String baseUrlText = baseUrl.getText();
        if (StringUtils.isBlank(baseUrlText)) {
            //提示需要填写YApi服务地址
            Messages.showYesNoDialog(FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_URL_TIP), "", Messages.getQuestionIcon());
            return null;
        }
        List<String> projectTokenList = ObjectUtils.listToList(fuTableComponent.getDataList(), YApiProjectTableData::getProjectToken);
        String value = Messages.showInputDialog(SYNC_TOKEN, SYNC_TOKEN_TITLE, Messages.getQuestionIcon(), StringUtils.EMPTY, new InputExistsValidator(projectTokenList));
        if (StringUtils.isEmpty(value)) {
            return null;
        }
        //根据token获取YApi项目信息
        YApiService yApiService = ServiceHelper.getService(YApiService.class);
        YApiProjectInfoDTO projectInfo = yApiService.findProjectInfo(baseUrlText, value);
        if (Objects.isNull(projectInfo)) {
            FuDocNotification.notifyError(FuDocMessageBundle.message(MessageConstants.SYNC_YAPI_GET_PROJECT_FAIL));
            return null;
        }
        YApiProjectTableData tableData = new YApiProjectTableData();
        tableData.setProjectId(projectInfo.getProjectId() + "");
        tableData.setProjectName(projectInfo.getProjectName());
        tableData.setProjectToken(value);
        tableData.setSelect(true);
        return tableData;
    }


    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        rootPane.setDefaultButton(this.loginBtn);
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("YApi", FuDocIcons.FU_API_YAPI, this.rootPane).builder();
    }

    private void createUIComponents() {
        this.baseUrl = new PlaceholderTextField("请输入你的YApi服务地址 例如:https://yapi.fudoc.com");
    }

    @Override
    public void apply() {
        //数据持久化
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        if (isEnable.isSelected()) {
            //如果开启了就设置启用的为yapi 否则不设置（都没有设置情况会有默认值）
            settingData.setEnable(ApiDocSystem.YAPI.getCode());
        }
        YapiConfigData yapi = settingData.getYapi();
        yapi.setBaseUrl(this.baseUrl.getText());
        yapi.setUserName(this.userName.getText());
        yapi.setYapiPwd(this.yapiPwd.getText());
        //获取当前需要保存的项目配置
        List<YApiProjectTableData> projectConfigList = this.fuTableComponent.getDataList();
        Project currProject = ProjectUtils.getCurrProject();
        String basePath = currProject.getBasePath();
        for (YApiProjectTableData yApiProjectTableData : projectConfigList) {
            Boolean select = yApiProjectTableData.getSelect();
            List<String> projectKeyList = yApiProjectTableData.getProjectKeyList();
            if (Objects.nonNull(select) && select) {
                projectKeyList.add(basePath);
            } else {
                projectKeyList.remove(basePath);
            }
        }
        yapi.setProjectConfigList(projectConfigList);
    }

    @Override
    public void reset() {
        FuDocSyncConfigData settingData = FuDocSyncSetting.getSettingData();
        //设置是否启用
        isEnable.setSelected(ApiDocSystem.YAPI.getCode().equals(settingData.getEnable()));
        YapiConfigData yapi = settingData.getYapi();
        this.baseUrl.setText(yapi.getBaseUrl());
        this.userName.setText(yapi.getUserName());
        this.yapiPwd.setText(yapi.getYapiPwd());
        Project currProject = ProjectUtils.getCurrProject();
        String basePath = currProject.getBasePath();
        List<YApiProjectTableData> projectConfigList = yapi.getProjectConfigList();
        if (CollectionUtils.isNotEmpty(projectConfigList)) {
            for (YApiProjectTableData yApiProjectTableData : projectConfigList) {
                List<String> projectKeyList = yApiProjectTableData.getProjectKeyList();
                yApiProjectTableData.setSelect(projectKeyList.contains(basePath));
            }
        }
        this.fuTableComponent.setDataList(projectConfigList);
    }
}
