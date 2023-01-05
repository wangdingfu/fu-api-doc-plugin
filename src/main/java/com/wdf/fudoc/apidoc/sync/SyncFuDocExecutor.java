package com.wdf.fudoc.apidoc.sync;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.strategy.SyncFuDocStrategy;
import com.wdf.fudoc.apidoc.sync.strategy.SyncShowDocStrategy;
import com.wdf.fudoc.apidoc.sync.strategy.SyncToYapiStrategy;
import com.wdf.fudoc.common.ServiceHelper;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2022-12-31 22:41:16
 */
public class SyncFuDocExecutor {


    private static final Map<ApiDocSystem, SyncFuDocStrategy> syncFuDocMap = new ConcurrentHashMap<>();

    static {
        syncFuDocMap.put(ApiDocSystem.YAPI, ServiceHelper.getService(SyncToYapiStrategy.class));
        syncFuDocMap.put(ApiDocSystem.SHOW_DOC, ServiceHelper.getService(SyncShowDocStrategy.class));
    }


    public static void sync(ApiDocSystem apiDocSystem, BaseSyncConfigData baseSyncConfigData, FuDocContext fuDocContext, PsiClass psiClass) {
        if (Objects.isNull(apiDocSystem)) {
            return;
        }
        SyncFuDocStrategy syncFuDocStrategy = syncFuDocMap.get(apiDocSystem);
        if (Objects.isNull(syncFuDocStrategy)) {
            return;
        }
        syncFuDocStrategy.syncFuDoc(fuDocContext, psiClass, baseSyncConfigData);
    }

}
