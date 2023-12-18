package com.wdf.fudoc.request.tab.settings;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.ide.BrowserUtil;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.Project;
import com.intellij.ui.AnActionButton;
import com.intellij.ui.IdeBorderFactory;
import com.intellij.ui.components.panels.VerticalBox;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.api.base.FuBundle;
import com.wdf.fudoc.common.FuDataTab;
import com.wdf.api.constants.MessageConstants;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.ConfigAuthTableBO;
import com.wdf.fudoc.request.pojo.ConfigEnvTableBO;
import com.wdf.fudoc.spring.SpringBootEnvLoader;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;
import org.jdesktop.swingx.action.OpenBrowserAction;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.util.List;

/**
 * 全局配置维护
 *
 * @author wangdingfu
 * @date 2022-12-07 21:48:14
 */
public class GlobalConfigTab implements FuDataTab<FuRequestConfigPO> {

    public static final String TITLE = "全局配置";

    private final VerticalBox rootBox;

    private final Project project;

    private final FuTableComponent<ConfigEnvTableBO> envTable;
    private final FuTableComponent<ConfigAuthTableBO> authTable;
    private static final TitledBorder envBorder = IdeBorderFactory.createTitledBorder(FuBundle.message(MessageConstants.REQUEST_GLOBAL_CONFIG_ENV_TITLE));
    private static final TitledBorder authBorder = IdeBorderFactory.createTitledBorder(FuBundle.message(MessageConstants.REQUEST_GLOBAL_CONFIG_AUTH_TITLE));

    public GlobalConfigTab(Project project) {
        this.project = project;
        this.envTable = FuTableComponent.create("env", FuTableColumnFactory.envConfig(), ConfigEnvTableBO.class);
        this.authTable = FuTableComponent.create("auth", FuTableColumnFactory.authConfig(), ConfigAuthTableBO.class);
        addReloadAction();
        this.rootBox = new VerticalBox();
        JPanel envPanel = this.envTable.createPanel();
        JPanel authPanel = this.authTable.createPanel();
        envPanel.setBorder(envBorder);
        authPanel.setBorder(authBorder);
        this.rootBox.add(envPanel);
        this.rootBox.add(authPanel);
    }


    private void addReloadAction() {
        this.envTable.addAnAction(new AnActionButton("Refresh", "", AllIcons.Actions.Refresh) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                SpringBootEnvLoader.doLoad(e.getProject(), true,true);
                FuRequestConfigPO fuRequestConfigPO = FuRequestConfigStorage.get(e.getProject()).readData();
                envTable.setDataList(Lists.newArrayList(fuRequestConfigPO.getEnvConfigList()));
            }
        });
        AnActionButton envAction = new AnActionButton("查看文档","",FuDocIcons.FU_DOC){
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                BrowserUtil.browse("http://www.fudoc.cn/pages/1fa915/");
            }
        };
        AnActionButton authAction = new AnActionButton("查看文档","",FuDocIcons.FU_DOC){
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                BrowserUtil.browse("http://www.fudoc.cn/pages/7881a4/");
            }
        };
        this.envTable.addAnAction(envAction);
        this.authTable.addAnAction(authAction);
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(TITLE, FuDocIcons.FU_SETTINGS, this.rootBox).builder();
    }

    @Override
    public void initData(FuRequestConfigPO data) {
        List<ConfigEnvTableBO> envConfigList = data.getEnvConfigList();
        List<ConfigAuthTableBO> authConfigList = data.getAuthConfigList();
        if (CollectionUtils.isNotEmpty(envConfigList)) {
            this.envTable.setDataList(Lists.newArrayList(envConfigList));
        }
        if (CollectionUtils.isNotEmpty(authConfigList)) {
            this.authTable.setDataList(Lists.newArrayList(authConfigList));
        }
    }

    @Override
    public void moveOff() {
        saveData(FuRequestConfigStorage.get(project).readData());
    }

    @Override
    public void saveData(FuRequestConfigPO data) {
        data.setEnvConfigList(Lists.newArrayList(this.envTable.getDataList()));
        data.setAuthConfigList(Lists.newArrayList(this.authTable.getDataList()));
    }
}
