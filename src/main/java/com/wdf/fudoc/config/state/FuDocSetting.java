package com.wdf.fudoc.config.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.ServiceManager;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.intellij.util.xmlb.XmlSerializerUtil;
import com.wdf.fudoc.data.CustomerSettingData;
import com.wdf.fudoc.data.SettingData;
import com.wdf.fudoc.util.ResourceUtils;
import lombok.Data;
import org.apache.commons.lang3.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

/**
 * Fu Doc 存储配置内容
 *
 * @author wangdingfu
 * @date 2022-08-06 01:17:22
 */
@Data
@State(name = "fuDocSetting", storages = {@Storage("FuDocSettings.xml")})
public class FuDocSetting implements PersistentStateComponent<FuDocSetting> {

    /**
     * 持久化数据
     */
    private SettingData settingData;


    public static SettingData getSettingData() {
        FuDocSetting fuDocSetting = ServiceManager.getService(FuDocSetting.class);
        if (Objects.isNull(fuDocSetting)) {
            return new SettingData();
        }
        SettingData settingData = fuDocSetting.settingData;
        if (Objects.isNull(settingData)) {
            settingData = new SettingData();
        }
        if (StringUtils.isBlank(settingData.getFuDocTemplateValue())) {
            settingData.setFuDocTemplateValue(ResourceUtils.readResource("template/fu_doc.ftl"));
        }
        if (StringUtils.isBlank(settingData.getObjectTemplateValue())) {
            settingData.setObjectTemplateValue(ResourceUtils.readResource("template/fu_doc_object.ftl"));
        }
        if (StringUtils.isBlank(settingData.getEnumTemplateValue1())) {
            settingData.setEnumTemplateValue1(ResourceUtils.readResource("template/fu_doc_enum.ftl"));
        }
        if (StringUtils.isBlank(settingData.getEnumTemplateValue2())) {
            settingData.setEnumTemplateValue2(ResourceUtils.readResource("template/fu_doc_enum_table.ftl"));
        }
        CustomerSettingData customerSettingData = settingData.getCustomerSettingData();
        if (Objects.isNull(customerSettingData)) {
            customerSettingData = new CustomerSettingData();
            settingData.setCustomerSettingData(customerSettingData);
        }
        return settingData;
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
