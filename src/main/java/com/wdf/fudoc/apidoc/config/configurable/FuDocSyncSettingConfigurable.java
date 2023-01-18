package com.wdf.fudoc.apidoc.config.configurable;

import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.util.NlsContexts;
import com.wdf.fudoc.apidoc.view.FuDocSyncSettingForm;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * 【Fu Doc】同步相关配置
 *
 * @author wangdingfu
 * @date 2023-01-06 23:07:18
 */
public class FuDocSyncSettingConfigurable implements SearchableConfigurable {

    private FuDocSyncSettingForm fuDocSyncSettingForm;


    @Override
    public @NotNull @NonNls String getId() {
        return "fu.doc.setting.sync";
    }

    @Override
    public @NlsContexts.ConfigurableName String getDisplayName() {
        return "Sync Settings";
    }

    @Override
    public @Nullable JComponent createComponent() {
        fuDocSyncSettingForm = new FuDocSyncSettingForm();
        return fuDocSyncSettingForm.getRootPanel();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() throws ConfigurationException {
        fuDocSyncSettingForm.apply();
    }

    @Override
    public void reset() {
        fuDocSyncSettingForm.reset();
    }
}
