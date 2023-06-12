package com.wdf.fudoc.apidoc.sync;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.strategy.SyncFuDocStrategy;
import com.wdf.fudoc.apidoc.sync.strategy.SyncShowDocStrategy;
import com.wdf.fudoc.apidoc.sync.strategy.SyncToApiFoxStrategy;
import com.wdf.fudoc.apidoc.sync.strategy.SyncToYApiStrategy;
import com.wdf.fudoc.common.ServiceHelper;
import com.wdf.fudoc.common.notification.FuDocNotification;

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
        syncFuDocMap.put(ApiDocSystem.YAPI, ServiceHelper.getService(SyncToYApiStrategy.class));
        syncFuDocMap.put(ApiDocSystem.SHOW_DOC, ServiceHelper.getService(SyncShowDocStrategy.class));
        syncFuDocMap.put(ApiDocSystem.API_FOX, ServiceHelper.getService(SyncToApiFoxStrategy.class));
    }


    public static void sync(ApiDocSystem apiDocSystem, BaseSyncConfigData baseSyncConfigData, FuDocContext fuDocContext, PsiClass psiClass) {
        if (Objects.isNull(apiDocSystem)) {
            return;
        }
        try {
            SyncFuDocStrategy syncFuDocStrategy = syncFuDocMap.get(apiDocSystem);
            if (Objects.isNull(syncFuDocStrategy)) {
                return;
            }
            syncFuDocStrategy.syncFuDoc(fuDocContext, psiClass, baseSyncConfigData);
        } catch (Exception e) {
            //发出提示 插件异常
            FuDocNotification.notifyError("同步接口文档至" + apiDocSystem.getCode() + "失败. 未知异常");
        }
    }

}
