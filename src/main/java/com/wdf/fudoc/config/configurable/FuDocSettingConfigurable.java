package com.wdf.fudoc.config.configurable;

import com.intellij.openapi.options.SearchableConfigurable;
import com.wdf.fudoc.config.state.FuDocSetting;
import com.wdf.fudoc.view.FuDocSettingForm;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangdingfu
 * @Description fu-doc 设置页面
 * @date 2022-08-06 00:42:46
 */
public class FuDocSettingConfigurable implements SearchableConfigurable {

    private FuDocSettingForm fuDocSettingForm;

    public FuDocSettingConfigurable() {

    }

    @Override
    public @NotNull @NonNls String getId() {
        return "fu.doc.setting";
    }

    @Override
    public @Nls(capitalization = Nls.Capitalization.Title) String getDisplayName() {
        return "Fu Doc";
    }


    /**
     * 将创建的UI面板交给Idea控制
     */
    @Override
    public @Nullable JComponent createComponent() {
        fuDocSettingForm = new FuDocSettingForm(FuDocSetting.getSettingData());
        return fuDocSettingForm.getRoot();
    }

    @Override
    public boolean isModified() {
        return true;
    }

    /**
     * 当点击配置完成时会粗发该方法
     * 一般主要用于将配置的信息实例化保存下来
     */
    @Override
    public void apply() {
        fuDocSettingForm.apply();
    }

    @Override
    public void reset() {
        fuDocSettingForm.reset();
    }
}
