package com.wdf.fudoc.apidoc.sync.data;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-06-12 21:36:09
 */
@Getter
@Setter
public class ApiFoxConfigData extends BaseSyncConfigData {

    /**
     * 用户token
     */
    private String token = "APS-XHZBI2EJ2WWkV5fmGvfgUNGCLEO8xOL5";

    public ApiFoxConfigData() {
        setBaseUrl("https://api.apifox.cn");
    }

    @Override
    public List<ApiProjectDTO> getProjectConfigList(String moduleName) {
        ApiProjectDTO apiProjectDTO = new ApiProjectDTO();
        apiProjectDTO.setProjectId("1756101");
        apiProjectDTO.setProjectName("测试项目");
        return Lists.newArrayList(apiProjectDTO);
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
