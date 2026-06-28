package com.wdf.fudoc.apidoc.sync.strategy;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;


/**
 * 将[Fu Doc]生成的接口文档同步至第三方文档系统中
 *
 * @author wangdingfu
 * @date 2022-12-31 15:51:27
 */
public interface SyncFuDocStrategy {

    /**
     * 同步接口文档到第三方文档系统中
     *
     * @param fuDocContext 上下文对象
     * @param psiClass     同步的接口所属的class对象
     */
    void syncFuDoc(FuDocContext fuDocContext, PsiClass psiClass, BaseSyncConfigData configData);


}
