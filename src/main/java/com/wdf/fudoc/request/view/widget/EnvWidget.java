package com.wdf.fudoc.request.view.widget;

import com.google.common.collect.Lists;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.wdf.fudoc.components.FuStatusLabel;
import com.wdf.fudoc.components.bo.DynamicTableBO;
import com.wdf.fudoc.components.listener.FuStatusLabelListener;
import com.wdf.fudoc.components.widget.FuWidget;
import com.wdf.fudoc.request.callback.FuRequestCallback;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.request.pojo.ConfigEnvTableBO;
import com.wdf.fudoc.spring.SpringConfigManager;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
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
    private final Module module;

    public EnvWidget(FuRequestCallback fuRequestCallback) {
        this(Objects.requireNonNull(ModuleUtil.findModuleForPsiElement(fuRequestCallback.getPsiElement())));
    }

    public EnvWidget(Module module) {
        this.module = module;
        this.configPO = FuRequestConfigStorageFactory.get(module.getProject()).readData();
        String envName = configPO.getEnvName();
        if (StringUtils.isBlank(envName)) {
            List<String> envConfigList = getList();
            envName = CollectionUtils.isNotEmpty(envConfigList) ? envConfigList.get(0) : StringUtils.EMPTY;
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
        return CollectionUtils.isNotEmpty(configPO.getEnvConfigList());
    }

    @Override
    public List<String> getList() {
        List<ConfigEnvTableBO> envConfigList = configPO.getEnvConfigList();
        if (CollectionUtils.isEmpty(envConfigList)) {
            return Lists.newArrayList();
        }
        String application = SpringConfigManager.getApplication(module);
        return envConfigList.stream().filter(DynamicTableBO::isSelect)
                .filter(f -> StringUtils.isNotBlank(f.getApplication()))
                .filter(f -> f.getApplication().equals(application))
                .map(ConfigEnvTableBO::getEnvName).collect(Collectors.toList());
    }

    @Override
    public void select(String text) {
        configPO.setEnvName(text);
    }
}
