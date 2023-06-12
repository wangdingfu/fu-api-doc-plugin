package com.wdf.fudoc.apidoc.sync.strategy;

import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;

import java.util.List;

/**
 * ApiFox同步文档策略
 * @author wangdingfu
 * @date 2023-06-12 21:39:05
 */
public class ApiFoxStrategy extends AbstractSyncFuDocStrategy{
    @Override
    protected boolean checkConfig(BaseSyncConfigData configData) {
        return true;
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
