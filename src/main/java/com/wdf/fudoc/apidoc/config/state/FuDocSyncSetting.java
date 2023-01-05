package com.wdf.fudoc.apidoc.config.state;

import com.intellij.openapi.components.PersistentStateComponent;
import com.intellij.openapi.components.State;
import com.intellij.openapi.components.Storage;
import com.wdf.fudoc.apidoc.sync.data.FuDocSyncConfigData;
import com.wdf.fudoc.common.ServiceHelper;
import lombok.Data;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author wangdingfu
 * @date 2022-12-31 22:55:01
 */
@Data
@State(name = "fuDocSyncSetting", storages = {@Storage("FuDocSyncSettings.xml")})
public class FuDocSyncSetting implements PersistentStateComponent<FuDocSyncConfigData> {

    private FuDocSyncConfigData fuDocSyncConfigData = new FuDocSyncConfigData();

    public static FuDocSyncSetting getInstance() {
        return ServiceHelper.getService(FuDocSyncSetting.class);
    }

    public static FuDocSyncConfigData getSettingData() {
        return getInstance().getState();
    }

    @Override
    public @Nullable FuDocSyncConfigData getState() {
        return this.fuDocSyncConfigData;
    }

    @Override
    public void loadState(@NotNull FuDocSyncConfigData state) {
        this.fuDocSyncConfigData = state;
    }
}
