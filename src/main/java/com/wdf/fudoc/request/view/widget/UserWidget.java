package com.wdf.fudoc.request.view.widget;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.project.Project;
import com.intellij.ui.IconManager;
import com.wdf.fudoc.components.FuStatusLabel;
import com.wdf.fudoc.components.bo.DynamicTableBO;
import com.wdf.fudoc.components.listener.FuStatusLabelListener;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.BasePopupMenuItem;
import com.wdf.fudoc.request.pojo.ConfigAuthTableBO;
import com.wdf.fudoc.request.tab.settings.GlobalConfigTab;
import com.wdf.fudoc.request.view.FuRequestSettingView;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-09 23:09:16
 */
public class UserWidget implements FuWidget, FuStatusLabelListener {

    private final FuStatusLabel fuStatusLabel;
    private final FuRequestConfigPO configPO;
    private final Project project;

    public UserWidget(Project project) {
        this.project = project;
        this.configPO = FuRequestConfigStorage.get(project).readData();
        this.fuStatusLabel = new FuStatusLabel(configPO.getUserName(), FuDocIcons.USER, this);
    }

    private static final String ADD_AUTH = "新增鉴权用户";

    @Override
    public String getCurrent() {
        return fuStatusLabel.getText();
    }

    @Override
    public JComponent getComponent() {
        return fuStatusLabel.getLabel();
    }

    @Override
    public boolean isShow() {
        return Objects.nonNull(fuStatusLabel) && CollectionUtils.isNotEmpty(configPO.getAuthConfigList());
    }

    @Override
    public List<BasePopupMenuItem> getList() {
        List<ConfigAuthTableBO> authConfigList = configPO.getAuthConfigList();
        if (CollectionUtils.isEmpty(authConfigList)) {
            return Lists.newArrayList();
        }
        Icon icon = IconManager.getInstance().getIcon("expui/general/User.svg", AllIcons.class);
        List<BasePopupMenuItem> resultList = Lists.newArrayList(new BasePopupMenuItem(icon, ADD_AUTH));
        authConfigList.stream().filter(DynamicTableBO::isSelect).forEach(f -> resultList.add(new BasePopupMenuItem(FuDocIcons.USER, f.getUserName())));
        return resultList;
    }

    @Override
    public void refresh() {
        String text = this.fuStatusLabel.getText();
        List<ConfigAuthTableBO> authConfigList = configPO.getAuthConfigList();
        if (StringUtils.isBlank(text)) {
            if (CollectionUtils.isNotEmpty(authConfigList)) {
                setText(authConfigList.stream().filter(ConfigAuthTableBO::getSelect).findFirst().orElse(authConfigList.get(0)).getUserName());
            }
            return;
        }
        if (CollectionUtils.isEmpty(authConfigList)) {
            setText(null);
            return;
        }
        if (authConfigList.stream().filter(ConfigAuthTableBO::getSelect).noneMatch(a -> a.getUserName().equals(text))) {
            setText(null);
            return;
        }
        setText(text);
    }

    private void setText(String userName) {
        fuStatusLabel.setText(userName);
        configPO.setUserName(userName);
    }

    @Override
    public void select(String text) {
        if (ADD_AUTH.equals(text)) {
            //跳转弹框配置环境
            FuRequestSettingView fuRequestSettingView = new FuRequestSettingView(project);
            fuRequestSettingView.setSize(900, 800);
            fuRequestSettingView.select(GlobalConfigTab.TITLE);
            fuRequestSettingView.show();
            return;
        }
        setText(text);
    }
}
