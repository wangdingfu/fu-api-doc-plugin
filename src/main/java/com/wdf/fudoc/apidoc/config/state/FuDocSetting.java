package com.wdf.fudoc.apidoc.config.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.wdf.fudoc.apidoc.data.SettingData;
import com.wdf.fudoc.common.ServiceHelper;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;


/**
 * Fu Doc 存储配置内容
 *
 * @author wangdingfu
 * @date 2022-08-06 01:17:22
 */
@Data
@State(name = "fuDocSetting", storages = {@Storage("FuDocSettings.xml")})
public class FuDocSetting implements PersistentStateComponent<SettingData> {

    /**
     * 持久化数据
     */
    private SettingData settingData = SettingData.defaultSettingData();

    public static FuDocSetting getInstance() {
        return ServiceHelper.getService(FuDocSetting.class);
    }

    public static SettingData getSettingData() {
        return getInstance().getState();
    }

    @Override
    public @Nullable SettingData getState() {
        this.settingData.defaultValue();
        return this.settingData;
    }

    @Override
    public void loadState(@NotNull SettingData state) {
        this.settingData = state;
    }
}
