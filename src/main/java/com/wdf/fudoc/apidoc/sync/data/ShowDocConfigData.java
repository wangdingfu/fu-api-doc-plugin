package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-01-08 20:29:16
 */
public class ShowDocConfigData extends BaseSyncConfigData{

    @Override
    public List<ApiProjectDTO> getProjectConfigList(String moduleName) {
        return null;
    }

    @Override
    public boolean isExistsConfig() {
        return false;
    }
}
