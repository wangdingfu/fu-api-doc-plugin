package com.wdf.fudoc.apidoc.sync.strategy;

import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;

import java.util.List;

/**
 * 同步接口文档到showDoc接口文档的实现
 *
 * @author wangdingfu
 * @date 2022-12-31 22:48:07
 */
public class SyncShowDocStrategy extends AbstractSyncSingleApiStrategy {


    @Override
    protected boolean checkConfig(BaseSyncConfigData configData) {
        return false;
    }

    @Override
    protected String doSingleApi(BaseSyncConfigData configData, FuDocItemData fuDocItemData, ApiProjectDTO apiProjectDTO, ApiCategoryDTO apiCategoryDTO) {
        return null;
    }

    @Override
    public ApiCategoryDTO createCategory(BaseSyncConfigData configData, ApiProjectDTO apiProjectDTO, String categoryName) {
        return null;
    }

    @Override
    public List<ApiCategoryDTO> categoryList(ApiProjectDTO apiProjectDTO, BaseSyncConfigData configData) {
        return null;
    }
}
