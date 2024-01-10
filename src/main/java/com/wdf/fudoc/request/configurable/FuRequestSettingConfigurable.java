package com.wdf.fudoc.request.configurable;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.NlsContexts;
import com.wdf.fudoc.request.view.FuRequestSettingView;
import com.wdf.api.util.ProjectUtils;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;
import java.util.Objects;

/**
 * 【Fu Request】设置界面
 *
 * @author wangdingfu
 * @date 2022-12-20 15:40:22
 */
public class FuRequestSettingConfigurable implements SearchableConfigurable {

    FuRequestSettingView fuRequestSettingView;

    @Override
    public @NotNull @NonNls String getId() {
        return "fu.request.setting";
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Fu Request";
    }

    @Override
    public @Nullable JComponent createComponent() {
        fuRequestSettingView = new FuRequestSettingView(ProjectUtils.getCurrProject());
        return fuRequestSettingView.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        if (Objects.nonNull(fuRequestSettingView)) {
            fuRequestSettingView.apply();
        }
    }

    @Override
    public void reset() {
        if (Objects.nonNull(fuRequestSettingView)) {
            fuRequestSettingView.initData();
        }
    }
}
