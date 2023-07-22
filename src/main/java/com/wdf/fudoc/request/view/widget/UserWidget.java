package com.wdf.fudoc.request.view.widget;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.components.FuStatusLabel;
import com.wdf.fudoc.components.bo.DynamicTableBO;
import com.wdf.fudoc.components.listener.FuStatusLabelListener;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.BasePopupMenuItem;
import com.wdf.fudoc.request.pojo.ConfigAuthTableBO;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-07-09 23:09:16
 */
public class UserWidget implements FuWidget, FuStatusLabelListener {

    private final FuStatusLabel fuStatusLabel;
    private final FuRequestConfigPO configPO;

    public UserWidget(Project project) {
        this.configPO = FuRequestConfigStorage.get(project).readData();
        this.fuStatusLabel = new FuStatusLabel(configPO.getUserName(), FuDocIcons.USER, this);
    }

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
        return authConfigList.stream().filter(DynamicTableBO::isSelect).map(f -> new BasePopupMenuItem(FuDocIcons.USER, f.getUserName())).collect(Collectors.toList());
    }

    @Override
    public void select(String text) {
        configPO.setUserName(text);
    }
}
