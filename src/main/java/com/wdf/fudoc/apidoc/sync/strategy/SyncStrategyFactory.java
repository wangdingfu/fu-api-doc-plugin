package com.wdf.fudoc.apidoc.sync.strategy;

import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.common.ServiceHelper;

/**
 * @author wangdingfu
 * @date 2023-01-08 23:28:34
 */
public class SyncStrategyFactory {

    public static SyncFuDocStrategy getInstance(String apiSystem) {
        if (ApiDocSystem.YAPI.getCode().equals(apiSystem)) {
            return ServiceHelper.getService(SyncToYApiStrategy.class);
        } else if (ApiDocSystem.SHOW_DOC.getCode().equals(apiSystem)) {
            return ServiceHelper.getService(SyncShowDocStrategy.class);
        }
        return null;
    }
}
