package com.wdf.fudoc.request.view.widget;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.components.FuStatusLabel;
import com.wdf.fudoc.components.bo.DynamicTableBO;
import com.wdf.fudoc.components.listener.FuStatusLabelListener;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.BasePopupMenuItem;
import com.wdf.fudoc.request.pojo.ConfigEnvTableBO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.tab.settings.GlobalConfigTab;
import com.wdf.fudoc.request.view.FuRequestSettingView;
import com.wdf.fudoc.spring.SpringConfigManager;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
import com.wdf.fudoc.util.ProjectUtils;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

/**
 * @author wangdingfu
 * @date 2023-07-09 23:09:16
 */
public class EnvWidget implements FuWidget, FuStatusLabelListener {

    private FuStatusLabel fuStatusLabel;
    private final FuRequestConfigPO configPO;
    private final RequestTabView requestTabView;

    private static final String CONFIG_ENV = "配置环境";

    public EnvWidget(Project project, RequestTabView requestTabView) {
        this.requestTabView = requestTabView;
        this.configPO = FuRequestConfigStorageFactory.get(project).readData();
        String envName = configPO.getEnvName();
        List<BasePopupMenuItem> envConfigList;
        if (StringUtils.isBlank(envName) && CollectionUtils.isNotEmpty(envConfigList = getList())) {
            Optional<BasePopupMenuItem> first = envConfigList.stream().filter(BasePopupMenuItem::isCanSelect).findFirst();
            if (first.isEmpty() || StringUtils.isBlank(envName = first.get().getShowName())) {
                return;
            }
        }
        this.fuStatusLabel = new FuStatusLabel(envName, FuDocIcons.SPRING_BOOT, this);
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
        return Objects.nonNull(this.fuStatusLabel) && CollectionUtils.isNotEmpty(configPO.getEnvConfigList());
    }

    @Override
    public List<BasePopupMenuItem> getList() {
        List<ConfigEnvTableBO> envConfigList = configPO.getEnvConfigList();
        if (CollectionUtils.isEmpty(envConfigList)) {
            return Lists.newArrayList();
        }
        String application = SpringConfigManager.getApplication(getModule());
        if (StringUtils.isBlank(application)) {
            return Lists.newArrayList();
        }
        List<BasePopupMenuItem> envList = Lists.newArrayList(new BasePopupMenuItem(AllIcons.General.Settings, CONFIG_ENV, false));
        envConfigList.stream().filter(DynamicTableBO::isSelect).filter(f -> StringUtils.isNotBlank(f.getApplication())).filter(f -> f.getApplication().equals(application)).forEach(f -> envList.add(new BasePopupMenuItem(FuDocIcons.SPRING_BOOT, f.getEnvName())));
        return envList;
    }

    @Override
    public void select(String text) {
        Module module = getModule();
        if (Objects.isNull(module)) {
            return;
        }
        if (CONFIG_ENV.equals(text)) {
            //跳转弹框配置环境
            FuRequestSettingView fuRequestSettingView = new FuRequestSettingView(module.getProject());
            fuRequestSettingView.setSize(800, 800);
            fuRequestSettingView.select(GlobalConfigTab.TITLE);
            fuRequestSettingView.show();
            return;
        }
        configPO.setEnvName(text);
        //更新http请求地址
        List<ConfigEnvTableBO> envConfigList = configPO.getEnvConfigList();
        if (CollectionUtils.isEmpty(envConfigList)) {
            return;
        }
        String application = SpringConfigManager.getApplication(module);
        envConfigList.stream().filter(f -> StringUtils.isNotBlank(f.getApplication()) && StringUtils.isNotBlank(f.getEnvName()) && StringUtils.isNotBlank(f.getDomain())).filter(f -> f.getApplication().equals(application)).filter(f -> f.getEnvName().equals(text)).findFirst().ifPresent(env -> {
            FuHttpRequestData fuHttpRequestData = requestTabView.getFuHttpRequestData();
            FuRequestData request = fuHttpRequestData.getRequest();
            request.setDomain(env.getDomain());
            requestTabView.setRequestUrl(request.getRequestUrl());
        });
    }


    private Module getModule() {
        FuHttpRequestData fuHttpRequestData = requestTabView.getFuHttpRequestData();
        if (Objects.nonNull(fuHttpRequestData)) {
            return fuHttpRequestData.getModule();
        }
        return null;
    }
}
