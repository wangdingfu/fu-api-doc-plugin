package com.wdf.fudoc.apidoc.sync.data;

import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import org.apache.commons.lang3.StringUtils;

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
    public void syncApiProjectList(String moduleName, List<ApiProjectDTO> apiProjectDTOList) {

    }

    @Override
    public boolean isExistsConfig() {
        return false;
    }

    @Override
    public void clearData(boolean isAll) {

    }

    @Override
    public ApiDocSystem getApiSystem() {
        return ApiDocSystem.SHOW_DOC;
    }

    @Override
    public String getApiDocUrl(SyncApiResultDTO syncApiResultDTO) {
        return StringUtils.EMPTY;
    }
}
