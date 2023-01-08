package com.wdf.fudoc.apidoc.sync.strategy;

import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.common.ServiceHelper;

/**
 * @author wangdingfu
 * @date 2023-01-08 23:28:34
 */
public class SyncStrategyFactory {

    public static SyncFuDocStrategy getInstance() {
        String enable = FuDocSyncSetting.getSettingData().getEnable();
        if (ApiDocSystem.YAPI.getCode().equals(enable)) {
            return ServiceHelper.getService(SyncToYApiStrategy.class);
        } else if (ApiDocSystem.SHOW_DOC.getCode().equals(enable)) {
            return ServiceHelper.getService(SyncShowDocStrategy.class);
        }
        return null;
    }
}
