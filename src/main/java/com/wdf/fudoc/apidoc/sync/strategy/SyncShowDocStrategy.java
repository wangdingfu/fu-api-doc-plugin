package com.wdf.fudoc.apidoc.sync.strategy;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.data.SyncApiData;
import com.wdf.fudoc.apidoc.sync.dto.AddApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiStructureTreeDTO;

import java.util.List;

/**
 * 同步接口文档到showDoc接口文档的实现
 *
 * @author wangdingfu
 * @date 2022-12-31 22:48:07
 */
public class SyncShowDocStrategy extends AbstractSyncFuDocStrategy {


    @Override
    protected boolean checkConfig(BaseSyncConfigData baseSyncConfigData) {
        return false;
    }

    @Override
    protected String doSync(BaseSyncConfigData configData, FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO) {
        return null;
    }

    @Override
    protected ApiProjectDTO getSyncProjectConfig(BaseSyncConfigData configData, PsiClass psiClass) {
        return null;
    }

    @Override
    public ApiCategoryDTO createCategory(BaseSyncConfigData baseSyncConfigData, ApiProjectDTO apiProjectDTO, String categoryName) {
        return null;
    }

    @Override
    public List<ApiCategoryDTO> categoryList(ApiProjectDTO apiProjectDTO, BaseSyncConfigData baseSyncConfigData) {
        return null;
    }

}
