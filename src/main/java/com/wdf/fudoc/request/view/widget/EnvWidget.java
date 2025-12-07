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
import com.wdf.fudoc.request.dialog.EnvQuickConfigDialog;
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
import com.wdf.fudoc.util.FuStringUtils;

import javax.swing.*;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;
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

    /**
     * 预编译正则表达式：用于清理环境名称中的环境类型后缀
     */
    private static final Pattern ENV_TYPE_SUFFIX_PATTERN = Pattern.compile("\\s*\\((开发|测试|生产)\\)$");

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
        List<BasePopupMenuItem> envList = Lists.newArrayList(
                new BasePopupMenuItem(AllIcons.General.Settings, CONFIG_ENV, false),
                new BasePopupMenuItem(AllIcons.General.Add, "+ 添加环境", false)
        );
        getEnvConfigList().forEach(f -> {
            // 根据环境类型显示不同的图标
            String envType = f.getEnvType();
            if ("dev".equalsIgnoreCase(envType)) {
                envList.add(new BasePopupMenuItem(FuDocIcons.SPRING_BOOT, f.getEnvName() + " (开发)"));
            } else if ("test".equalsIgnoreCase(envType)) {
                envList.add(new BasePopupMenuItem(FuDocIcons.SPRING_BOOT, f.getEnvName() + " (测试)"));
            } else if ("prod".equalsIgnoreCase(envType)) {
                envList.add(new BasePopupMenuItem(FuDocIcons.SPRING_BOOT, f.getEnvName() + " (生产)"));
            } else {
                envList.add(new BasePopupMenuItem(FuDocIcons.SPRING_BOOT, f.getEnvName()));
            }
        });
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

        if ("+ 添加环境".equals(text)) {
            // 显示快速添加环境对话框
            ConfigEnvTableBO newEnv = EnvQuickConfigDialog.showEnvConfigDialog(module.getProject(), configPO.getEnvConfigList());
            if (newEnv != null) {
                SpringBootEnvModuleInfo.SpringBootEnvInfo envInfo = SpringBootEnvLoader.getEnvInfo(module);
                if (envInfo != null) {
                    newEnv.setApplication(envInfo.getApplicationName());
                }
                configPO.getEnvConfigList().add(newEnv);
                FuRequestConfigStorage.get(module.getProject()).saveData(configPO);

                // 更新标签显示，但不调用select避免递归
                this.fuStatusLabel.setText(newEnv.getEnvName());

                // 自动选择新添加的环境（更新配置，但不再调用refresh）
                updateEnvironmentConfig(newEnv.getEnvName(), module);
            }
            return;
        }

        // 清理环境名称中的特定后缀（只清理 "(开发)"、"(测试)"、"(生产)"）
        String cleanEnvName = ENV_TYPE_SUFFIX_PATTERN.matcher(text).replaceAll("");

        configPO.addDefaultEnv(module.getName(), cleanEnvName);

        //更新http请求地址
        List<ConfigEnvTableBO> envConfigList = getEnvConfigList();
        if (CollectionUtils.isEmpty(envConfigList)) {
            return;
        }

        envConfigList.stream().filter(f -> f.getEnvName().equals(cleanEnvName)).findFirst().ifPresent(env -> {
            // 更新环境配置
            updateEnvironmentRequest(env);

            // 更新标签显示
            this.fuStatusLabel.setText(cleanEnvName);
        });
    }

    /**
     * 更新环境配置
     */
    private void updateEnvironmentConfig(String envName, Module module) {
        List<ConfigEnvTableBO> envConfigList = getEnvConfigList();
        if (CollectionUtils.isEmpty(envConfigList)) {
            return;
        }

        envConfigList.stream()
            .filter(f -> f.getEnvName().equals(envName))
            .findFirst()
            .ifPresent(this::updateEnvironmentRequest);
    }

    /**
     * 更新环境请求配置
     */
    private void updateEnvironmentRequest(ConfigEnvTableBO env) {
        FuHttpRequestData fuHttpRequestData = requestTabView.getFuHttpRequestData();
        if (fuHttpRequestData == null) {
            return;
        }

        FuRequestData request = fuHttpRequestData.getRequest();
        if (request == null) {
            return;
        }

        // 使用工具类构建完整 URL
        String domain = com.wdf.fudoc.util.UrlBuildUtils.buildFullUrl(env.getDomain(), env.getPort());
        request.setDomain(domain);

        // 设置上下文路径（规范化处理）
        request.setContextPath(com.wdf.fudoc.util.UrlBuildUtils.normalizeContextPath(env.getContextPath()));

        request.setRequestUrl(null);
        requestTabView.setRequestUrl(request.getRequestUrl());
    }


    @Override
    public void refresh() {
        String currentText = this.fuStatusLabel.getText();
        String envName = getEnvName();

        // 如果当前没有文本或环境名为空，设置环境名
        if (FuStringUtils.isBlank(currentText) || FuStringUtils.isBlank(envName)) {
            this.fuStatusLabel.setText(envName);
            return;
        }

        List<ConfigEnvTableBO> envConfigList = getEnvConfigList();
        if (CollectionUtils.isEmpty(envConfigList)) {
            this.fuStatusLabel.setText(null);
            return;
        }

        // 检查当前环境是否仍然有效
        boolean envExists = envConfigList.stream().anyMatch(a -> a.getEnvName().equals(currentText));
        if (!envExists) {
            // 当前环境不存在，切换到默认环境
            this.fuStatusLabel.setText(envName);
        }
    }

    private void setText(String text) {
        this.fuStatusLabel.setText(text);
        // 移除这里的select调用，避免无限递归
        // select()方法会在外部需要时被调用
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
                .filter(f -> FuStringUtils.isNotBlank(f.getApplication()))
                .filter(DynamicTableBO::isSelect)
                .filter(f -> FuStringUtils.isNotBlank(f.getDomain()))
                .filter(f -> FuStringUtils.isNotBlank(f.getEnvName()))
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
