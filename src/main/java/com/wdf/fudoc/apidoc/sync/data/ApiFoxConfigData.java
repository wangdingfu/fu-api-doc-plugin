package com.wdf.fudoc.apidoc.sync.data;

import com.google.common.collect.Lists;
import com.intellij.openapi.module.Module;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncProjectSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.data.SyncApiConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import com.wdf.api.constants.UrlConstants;
import com.wdf.fudoc.spring.SpringBootEnvLoader;
import com.wdf.api.util.JsonUtil;
import com.wdf.fudoc.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-06-12 21:36:09
 */
@Getter
@Setter
public class ApiFoxConfigData extends BaseSyncConfigData {

    /**
     * 用户token APS-XHZBI2EJ2WWkV5fmGvfgUNGCLEO8xOL5
     */
    private String token;

    @Override
    public String getBaseUrl() {
        return StringUtils.isBlank(super.baseUrl) ? UrlConstants.API_FOX : super.baseUrl;
    }

    @Override
    public boolean isAutoGenCategory() {
        return true;
    }

    public ApiFoxConfigData() {
    }

    @Override
    public List<ApiProjectDTO> getProjectConfigList(Module module) {
        SyncApiConfigData state = FuDocSyncProjectSetting.getInstance().getState();
        if (Objects.isNull(state)) {
            return Lists.newArrayList();
        }
        String application = SpringBootEnvLoader.getApplication(module);
        return state.getApiFoxConfigList().stream().filter(f -> StringUtils.isNotBlank(f.getApplicationName()))
                .filter(f -> f.getApplicationName().equals(application))
                .map(this::buildApiProjectDTO).collect(Collectors.toList());
    }

    @Override
    public void syncApiProjectList(Module module, List<ApiProjectDTO> apiProjectDTOList) {
        SyncApiConfigData state = FuDocSyncProjectSetting.getInstance().getState();
        if (Objects.isNull(state)) {
            state = new SyncApiConfigData();
            FuDocSyncProjectSetting.getInstance().loadState(state);
        }
        String application = SpringBootEnvLoader.getApplication(module);
        List<ApiFoxProjectTableData> apiFoxConfigList = state.getApiFoxConfigList();
        apiFoxConfigList.removeIf(f -> f.getApplicationName().equals(application));
        apiFoxConfigList.addAll(ObjectUtils.listToList(apiProjectDTOList, this::buildConfigData));
        FuDocSyncProjectSetting.getInstance().loadState(state);
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
        return "https://app.apifox.com/project/" + syncApiResultDTO.getProjectId();
    }


    private ApiProjectDTO buildApiProjectDTO(ApiFoxProjectTableData tableData) {
        ApiProjectDTO apiProjectDTO = new ApiProjectDTO();
        apiProjectDTO.setProjectId(tableData.getProjectId());
        apiProjectDTO.setProjectName(tableData.getProjectName());
        apiProjectDTO.setApiCategoryList(JsonUtil.toList(tableData.getCategories(), ApiCategoryDTO.class));
        apiProjectDTO.setApplicationName(tableData.getApplicationName());
        return apiProjectDTO;
    }

    private ApiFoxProjectTableData buildConfigData(ApiProjectDTO projectDTO) {
        ApiFoxProjectTableData configData = new ApiFoxProjectTableData();
        configData.setProjectId(projectDTO.getProjectId());
        configData.setProjectName(projectDTO.getProjectName());
        configData.setCategories(JsonUtil.toJson(projectDTO.getApiCategoryList()));
        configData.setApplicationName(projectDTO.getApplicationName());
        return configData;
    }
}
