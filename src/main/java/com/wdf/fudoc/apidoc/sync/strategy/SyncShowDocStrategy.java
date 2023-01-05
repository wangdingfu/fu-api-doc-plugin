package com.wdf.fudoc.apidoc.sync.strategy;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiData;
import com.wdf.fudoc.apidoc.sync.dto.ApiStructureTreeDTO;

/**
 * 同步接口文档到showDoc接口文档的实现
 * @author wangdingfu
 * @date 2022-12-31 22:48:07
 */
public class SyncShowDocStrategy extends AbstractSyncFuDocStrategy{

    @Override
    protected void checkConfig(BaseSyncConfigData baseSyncConfigData) {

    }

    @Override
    protected String assembleSyncData(SyncApiData syncApiData) {
        return null;
    }

    @Override
    protected String checkSyncResult(String result) {
        return null;
    }

    @Override
    public ApiStructureTreeDTO getApiStructureTree(PsiClass psiClass) {
        return null;
    }
}
