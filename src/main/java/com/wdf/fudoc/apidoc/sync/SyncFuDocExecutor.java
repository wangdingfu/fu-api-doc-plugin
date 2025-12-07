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
import cn.fudoc.common.notification.FuDocNotification;
import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2022-12-31 22:41:16
 */
@Slf4j
public class SyncFuDocExecutor {

    /**
     * 延迟初始化的服务缓存 Map
     * IDEA 2025.1+ 不允许在静态初始化块中获取服务实例,改为按需获取
     */
    private static final Map<ApiDocSystem, SyncFuDocStrategy> syncFuDocMap = new ConcurrentHashMap<>();

    /**
     * 获取同步策略服务实例(延迟加载)
     *
     * @param apiDocSystem API 文档系统类型
     * @return 对应的同步策略实例
     */
    private static SyncFuDocStrategy getSyncStrategy(ApiDocSystem apiDocSystem) {
        return syncFuDocMap.computeIfAbsent(apiDocSystem, system -> {
            return switch (system) {
                case YAPI -> ServiceHelper.getService(SyncToYApiStrategy.class);
                case SHOW_DOC -> ServiceHelper.getService(SyncShowDocStrategy.class);
                case API_FOX -> ServiceHelper.getService(SyncToApiFoxStrategy.class);
                default -> null;
            };
        });
    }


    public static void sync(ApiDocSystem apiDocSystem, BaseSyncConfigData baseSyncConfigData, FuDocContext fuDocContext, PsiClass psiClass) {
        if (Objects.isNull(apiDocSystem)) {
            return;
        }
        try {
            SyncFuDocStrategy syncFuDocStrategy = getSyncStrategy(apiDocSystem);
            if (Objects.isNull(syncFuDocStrategy)) {
                return;
            }
            syncFuDocStrategy.syncFuDoc(fuDocContext, psiClass, baseSyncConfigData);
        } catch (Exception e) {
            //发出提示 插件异常
            String errorMsg = "同步接口文档至" + apiDocSystem.getCode() + "失败. 未知异常";
            log.info(errorMsg, e);
            FuDocNotification.notifyError(errorMsg);
        }
    }

}
