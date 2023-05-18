package com.wdf.fudoc.storage.handler;

import com.wdf.fudoc.apidoc.config.state.FuDocSyncProjectSetting;
import com.wdf.fudoc.apidoc.data.SyncApiConfigData;
import com.wdf.fudoc.storage.enumtype.FuStorageType;

/**
 * @author wangdingfu
 * @date 2023-05-15 21:26:28
 */
public class FuSyncApiProjectStorage extends AbstractJsonDataStorage<SyncApiConfigData>{
    public static final FuSyncApiProjectStorage INSTANCE = new FuSyncApiProjectStorage();

    @Override
    public FuStorageType getType() {
        return FuStorageType.FU_CONFIG_YAPI;
    }
}
