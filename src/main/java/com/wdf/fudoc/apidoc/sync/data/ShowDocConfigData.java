package com.wdf.fudoc.apidoc.sync.data;

import com.google.common.collect.Lists;
import com.intellij.openapi.module.Module;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncProjectSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.data.SyncApiConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import cn.fudoc.common.constants.UrlConstants;
import com.wdf.fudoc.spring.SpringBootEnvLoader;
import cn.fudoc.common.util.JsonUtil;
import com.wdf.fudoc.util.ObjectUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-01-08 20:29:16
 */
public class ShowDocConfigData extends BaseSyncConfigData {

    @Override
    public String getBaseUrl() {
        return FuStringUtils.isBlank(super.baseUrl) ? UrlConstants.SHOW_DOC : super.baseUrl;
    }

    @Override
    public List<ApiProjectDTO> getProjectConfigList(Module module) {
        SyncApiConfigData state = FuDocSyncProjectSetting.getInstance().getState();
        if (Objects.isNull(state)) {
            return Lists.newArrayList();
        }
        String application = SpringBootEnvLoader.getApplication(module);
        return state.getShowDocConfigList().stream().filter(f -> FuStringUtils.isNotBlank(f.getApplicationName()))
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

        List<ShowDocProjectTableData> showDocConfigList = state.getShowDocConfigList();
        showDocConfigList.removeIf(f -> application.equals(f.getApplicationName()));
        showDocConfigList.addAll(ObjectUtils.listToList(apiProjectDTOList, this::buildConfigData));
        FuDocSyncProjectSetting.getInstance().loadState(state);
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
        return super.baseUrl;
    }


    private ApiProjectDTO buildApiProjectDTO(ShowDocProjectTableData tableData) {
        ApiProjectDTO apiProjectDTO = new ApiProjectDTO();
        apiProjectDTO.setProjectId(tableData.getApiKey());
        apiProjectDTO.setProjectToken(tableData.getApiToken());
        apiProjectDTO.setProjectName(tableData.getProjectName());
        apiProjectDTO.setApiCategoryList(JsonUtil.toList(tableData.getCategories(), ApiCategoryDTO.class));
        apiProjectDTO.setApplicationName(tableData.getApplicationName());
        apiProjectDTO.setLatest(tableData.isLatest());
        return apiProjectDTO;
    }

    private ShowDocProjectTableData buildConfigData(ApiProjectDTO projectDTO) {
        ShowDocProjectTableData configData = new ShowDocProjectTableData();
        configData.setApiKey(projectDTO.getProjectId());
        configData.setApiToken(projectDTO.getProjectToken());
        configData.setProjectName(projectDTO.getProjectName());
        configData.setCategories(JsonUtil.toJson(projectDTO.getApiCategoryList()));
        configData.setApplicationName(projectDTO.getApplicationName());
        configData.setLatest(projectDTO.isLatest());
        return configData;
    }
}
