package com.wdf.fudoc.config.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wdf.fudoc.FuDocMessageBundle;
import com.wdf.fudoc.data.SettingData;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Fu Doc 存储配置内容
 *
 * @author wangdingfu
 * @Date 2022-08-06 01:17:22
 */
@Getter
@Setter
@State(name = "fuDocSetting", storages = {@Storage("FuDocSettings.xml")})
public class FuDocSetting implements PersistentStateComponent<FuDocSetting> {

    /**
     * 动态值
     */
    private SettingData settingData;


    public SettingData getSettingData() {
        if (Objects.isNull(this.settingData)) {
            this.settingData = new SettingData();
        }
        if(StringUtils.isBlank(this.settingData.getFuDocTemplateValue())){
            this.settingData.setFuDocTemplateValue(FuDocMessageBundle.message("template"));
        }
        if(StringUtils.isBlank(this.settingData.getObjectTemplateValue())){
            this.settingData.setObjectTemplateValue(FuDocMessageBundle.message("template.object"));
        }
        if(StringUtils.isBlank(this.settingData.getEnumTemplateValue1())){
            this.settingData.setEnumTemplateValue1(FuDocMessageBundle.message("template.enum1"));
        }
        if(StringUtils.isBlank(this.settingData.getEnumTemplateValue2())){
            this.settingData.setEnumTemplateValue1(FuDocMessageBundle.message("template.enum2"));
        }
        return this.settingData;
    }

    public static FuDocSetting getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, FuDocSetting.class);
    }

    @Override
    public @Nullable FuDocSetting getState() {
        return this;
    }

    @Override
    public void loadState(@NotNull FuDocSetting state) {
        XmlSerializerUtil.copyBean(state, this);
    }
}
