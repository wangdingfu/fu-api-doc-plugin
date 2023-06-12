package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-06-12 21:36:09
 */
public class ApiFoxConfigData extends BaseSyncConfigData {


    @Override
    public List<ApiProjectDTO> getProjectConfigList(String moduleName) {
        return null;
    }

    @Override
    public boolean isExistsConfig() {
        return true;
    }

    @Override
    public void clearData(boolean isAll) {

    }

    @Override
    public ApiDocSystem getApiSystem() {
        return ApiDocSystem.API_FOX;
    }

    @Override
    public String getApiDocUrl(SyncApiResultDTO syncApiResultDTO) {

        return null;
    }
}
