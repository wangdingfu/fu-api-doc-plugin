package com.wdf.fudoc.config.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.openapi.project.Project;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wdf.fudoc.FuDocMessageBundle;
import com.wdf.fudoc.data.SettingData;
import com.wdf.fudoc.util.ResourceUtils;
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
public class FuDocSetting implements PersistentStateComponent<SettingData> {

    /**
     * 动态值
     */
    private SettingData settingData;


    public static FuDocSetting getInstance(@NotNull Project project) {
        return ServiceManager.getService(project, FuDocSetting.class);
    }

    @Override
    public @Nullable SettingData getState() {
        if (Objects.isNull(this.settingData)) {
            this.settingData = new SettingData();
        }
        if (StringUtils.isBlank(this.settingData.getFuDocTemplateValue())) {
            this.settingData.setFuDocTemplateValue(ResourceUtils.readResource("template/fu_doc.ftl"));
        }
        if (StringUtils.isBlank(this.settingData.getObjectTemplateValue())) {
            this.settingData.setObjectTemplateValue(ResourceUtils.readResource("template/fu_doc_object.ftl"));
        }
        if (StringUtils.isBlank(this.settingData.getEnumTemplateValue1())) {
            this.settingData.setEnumTemplateValue1(ResourceUtils.readResource("template/fu_doc_enum.ftl"));
        }
        if (StringUtils.isBlank(this.settingData.getEnumTemplateValue2())) {
            this.settingData.setEnumTemplateValue2(ResourceUtils.readResource("template/fu_doc_enum_table.ftl"));
        }
        return this.settingData;
    }

    @Override
    public void loadState(@NotNull SettingData state) {
        XmlSerializerUtil.copyBean(state, Objects.requireNonNull(getState()));
    }
}
