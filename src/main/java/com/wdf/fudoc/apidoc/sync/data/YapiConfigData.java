package com.wdf.fudoc.apidoc.sync.data;

import com.intellij.openapi.module.Module;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncProjectSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import com.wdf.fudoc.components.bo.TreePathBO;
import com.wdf.fudoc.spring.SpringBootEnvLoader;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Comparator;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Yapi配置
 *
 * @author wangdingfu
 * @date 2023-01-05 11:52:04
 */
@Getter
@Setter
public class YapiConfigData extends BaseSyncConfigData {

    /**
     * 用户名
     */
    private String userName;

    /**
     * 密码
     */
    private String yapiPwd;


    @Override
    public List<ApiProjectDTO> getProjectConfigList(Module module) {
        String application = SpringBootEnvLoader.getApplication(module);
        return FuDocSyncProjectSetting.getYapiConfigList().stream()
                .filter(f -> StringUtils.isNotBlank(f.getApplicationName()))
                .filter(f -> f.getApplicationName().equals(application)).map(this::convert).collect(Collectors.toList());
    }

    @Override
    public void syncApiProjectList(Module module, List<ApiProjectDTO> apiProjectDTOList) {

    }

    @Override
    public boolean isExistsConfig() {
        return CollectionUtils.isNotEmpty(FuDocSyncProjectSetting.getYapiConfigList());
    }

    @Override
    public void clearData(boolean isAll) {

    }

    @Override
    public ApiDocSystem getApiSystem() {
        return ApiDocSystem.YAPI;
    }

    @Override
    public String getApiDocUrl(SyncApiResultDTO syncApiResultDTO) {
        String projectId = syncApiResultDTO.getProjectId();
        if (StringUtils.isBlank(projectId)) {
            return getBaseUrl();
        }
        String apiUrl = getBaseUrl() + "project/" + syncApiResultDTO.getProjectId() + "/interface/api/";
        if (StringUtils.isNotBlank(syncApiResultDTO.getApiId())) {
            return apiUrl + syncApiResultDTO.getApiId();
        }
        if (StringUtils.isNotBlank(syncApiResultDTO.getCategoryId())) {
            return apiUrl + "cat_" + syncApiResultDTO.getCategoryId();
        }
        return apiUrl;
    }


    private ApiProjectDTO convert(YApiProjectTableData tableData) {
        ApiProjectDTO apiProjectDTO = new ApiProjectDTO();
        apiProjectDTO.setProjectToken(tableData.getProjectToken());
        apiProjectDTO.setProjectId(tableData.getProjectId());
        apiProjectDTO.setProjectName(tableData.getProjectName());
        apiProjectDTO.setApplicationName(tableData.getApplicationName());
        apiProjectDTO.setLatest(tableData.isLatest());
        return apiProjectDTO;
    }
}
