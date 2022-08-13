package com.wdf.fudoc.config.configurable;

import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.config.state.FuDocSetting;
import com.wdf.fudoc.data.SettingData;
import com.wdf.fudoc.view.FuDocGeneralSettingForm;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-08-07 23:34:03
 */
public class FuDocGeneralSettingConfigurable implements SearchableConfigurable {

    private final Project project;

    private FuDocGeneralSettingForm fuDocGeneralSettingForm;

    public FuDocGeneralSettingConfigurable(Project project) {
        this.project = project;
    }

    @Override
    public @NotNull @NonNls String getId() {
        return "fu.doc.setting.config";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return null;
    }

    @Override
    public @Nullable JComponent createComponent() {
        FuDocSetting fuDocSetting = FuDocSetting.getInstance(this.project);
        SettingData settingData = fuDocSetting.getSettingData();
        fuDocGeneralSettingForm = new FuDocGeneralSettingForm(project, settingData.getCustomerSettingData());
        return fuDocGeneralSettingForm.getRoot();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    @Override
    public void apply() {
        fuDocGeneralSettingForm.apply();
    }
}
