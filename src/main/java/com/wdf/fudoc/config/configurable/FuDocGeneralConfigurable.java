package com.wdf.fudoc.config.configurable;

import com.intellij.openapi.options.SearchableConfigurable;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.config.state.FuDocSetting;
import com.wdf.fudoc.data.CustomerSettingData;
import com.wdf.fudoc.view.FuDocGeneralForm;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-08-14 21:11:10
 */
public class FuDocGeneralConfigurable implements SearchableConfigurable {

    /**
     * 基础设置页面
     */
    private FuDocGeneralForm fuDocGeneralForm;

    /**
     * 标识项目
     */
    private final Project project;


    /**
     * 在xml中配置了ProjectConfigurable 系统会调用该方法
     */
    public FuDocGeneralConfigurable(Project project) {
        this.project = project;
    }

    @Override
    public @NotNull @NonNls String getId() {
        return "fu.doc.setting.general";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "General";
    }


    @Override
    public @Nullable JComponent createComponent() {
        CustomerSettingData customerSettingData = FuDocSetting.getSettingData(project).getCustomerSettingData();
        fuDocGeneralForm = new FuDocGeneralForm(project, customerSettingData);
        return fuDocGeneralForm.getRootPanel();
    }


    /**
     * 判断页面数据是否修改了
     * 返回true 被修改了才会调用apply
     */
    @Override
    public boolean isModified() {
        return true;
    }


    /**
     * 点击[OK]或则[apply] 时会被调用
     */
    @Override
    public void apply() {
        fuDocGeneralForm.apply();
    }

    @Override
    public void reset() {
        fuDocGeneralForm.reset();
    }
}
