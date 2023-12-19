package com.wdf.fudoc.request.view.widget;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.components.FuStatusLabel;
import com.wdf.fudoc.components.bo.DynamicTableBO;
import com.wdf.fudoc.components.listener.FuStatusLabelListener;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.callback.FuRequestCallback;
import com.wdf.fudoc.request.http.FuRequest;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.BasePopupMenuItem;
import com.wdf.fudoc.request.pojo.ConfigEnvTableBO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;
import com.wdf.fudoc.request.tab.settings.GlobalConfigTab;
import com.wdf.fudoc.request.view.FuRequestSettingView;
import com.wdf.fudoc.spring.SpringBootEnvLoader;
import com.wdf.fudoc.spring.SpringBootEnvModuleInfo;
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
public class EnvWidget implements FuWidget, FuStatusLabelListener {

    private final FuStatusLabel fuStatusLabel;
    private final FuRequestConfigPO configPO;
    private final RequestTabView requestTabView;
    private final FuRequestCallback fuRequestCallback;

    private static final String CONFIG_ENV = "配置环境";

    public EnvWidget(Project project, RequestTabView requestTabView, FuRequestCallback fuRequestCallback) {
        this.requestTabView = requestTabView;
        this.configPO = FuRequestConfigStorage.get(project).readData();
        this.fuStatusLabel = new FuStatusLabel(getEnvName(), FuDocIcons.SPRING_BOOT, this);
        this.fuRequestCallback = fuRequestCallback;
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
        List<BasePopupMenuItem> envList = Lists.newArrayList(new BasePopupMenuItem(AllIcons.General.Settings, CONFIG_ENV, false));
        getEnvConfigList().forEach(f -> envList.add(new BasePopupMenuItem(FuDocIcons.SPRING_BOOT, f.getEnvName())));
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
            FuRequestSettingView fuRequestSettingView = new FuRequestSettingView(module.getProject(), fuRequestCallback);
            fuRequestSettingView.setSize(900, 800);
            fuRequestSettingView.select(GlobalConfigTab.TITLE);
            fuRequestSettingView.show();
            return;
        }
        configPO.addDefaultEnv(module.getName(), text);
        //更新http请求地址
        List<ConfigEnvTableBO> envConfigList = getEnvConfigList();
        if (CollectionUtils.isEmpty(envConfigList)) {
            return;
        }
        envConfigList.stream().filter(f -> f.getEnvName().equals(text)).findFirst().ifPresent(env -> {
            FuHttpRequestData fuHttpRequestData = requestTabView.getFuHttpRequestData();
            FuRequestData request = fuHttpRequestData.getRequest();
            request.setDomain(env.getDomain());
            request.setContextPath(null);
            request.setRequestUrl(null);
            requestTabView.setRequestUrl(request.getRequestUrl());
        });
    }


    @Override
    public void refresh() {
        String text = this.fuStatusLabel.getText();
        if (StringUtils.isBlank(text)) {
            setText(getEnvName());
            return;
        }
        List<ConfigEnvTableBO> envConfigList = getEnvConfigList();
        if (CollectionUtils.isEmpty(envConfigList)) {
            this.fuStatusLabel.setText(null);
            return;
        }
        if (envConfigList.stream().noneMatch(a -> a.getEnvName().equals(text))) {
            setText(getEnvName());
            return;
        }
        setText(text);
    }

    private void setText(String text) {
        this.fuStatusLabel.setText(text);
        if (StringUtils.isNotBlank(text)) {
            select(text);
        }
    }

    private List<ConfigEnvTableBO> getEnvConfigList() {
        List<ConfigEnvTableBO> envConfigList = configPO.getEnvConfigList();
        if (CollectionUtils.isEmpty(envConfigList)) {
            return Lists.newArrayList();
        }
        SpringBootEnvModuleInfo.SpringBootEnvInfo envInfo = SpringBootEnvLoader.getEnvInfo(getModule());
        if (Objects.isNull(envInfo)) {
            return Lists.newArrayList();
        }
        String applicationName = envInfo.getApplicationName();
        return envConfigList.stream()
                .filter(f -> StringUtils.isNotBlank(f.getApplication()))
                .filter(DynamicTableBO::isSelect)
                .filter(f -> StringUtils.isNotBlank(f.getDomain()))
                .filter(f -> StringUtils.isNotBlank(f.getEnvName()))
                .filter(f -> f.getApplication().equals(applicationName)).collect(Collectors.toList());
    }


    private Module getModule() {
        FuHttpRequestData fuHttpRequestData = requestTabView.getFuHttpRequestData();
        if (Objects.nonNull(fuHttpRequestData)) {
            return fuHttpRequestData.getModule();
        }
        return null;
    }


    private String getEnvName() {
        Module module = getModule();
        String moduleName = Objects.isNull(module) ? null : module.getName();
        return configPO.getEnv(moduleName);
    }
}
