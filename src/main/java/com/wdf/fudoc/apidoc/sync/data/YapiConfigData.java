package com.wdf.fudoc.apidoc.sync.data;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.constant.enumtype.YesOrNo;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import com.wdf.fudoc.components.bo.TreePathBO;
import com.wdf.fudoc.util.ProjectUtils;
import lombok.Getter;
import lombok.Setter;
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

    /**
     * 项目配置
     */
    private List<YApiProjectTableData> projectConfigList = Lists.newArrayList();


    @Override
    public List<ApiProjectDTO> getProjectConfigList(String moduleName) {
        String basePath = ProjectUtils.getCurrentProjectPath();
        return this.projectConfigList.stream().filter(f -> f.getProjectKeyList().contains(basePath)).map(m -> convert(m, moduleName))
                //过滤没匹配上的module sort=0则是没有匹配上
                .filter(f -> YesOrNo.NO.getCode() != f.getSort()).sorted(Comparator.comparing(ApiProjectDTO::getSort)).collect(Collectors.toList());
    }

    @Override
    public boolean isExistsConfig() {
        String basePath = ProjectUtils.getCurrentProjectPath();
        return this.projectConfigList.stream().anyMatch(a -> a.getProjectKeyList().contains(basePath));
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


    private ApiProjectDTO convert(YApiProjectTableData tableData, String moduleName) {
        ApiProjectDTO apiProjectDTO = new ApiProjectDTO();
        apiProjectDTO.setProjectToken(tableData.getProjectToken());
        apiProjectDTO.setProjectId(tableData.getProjectId());
        apiProjectDTO.setProjectName(tableData.getProjectName());
        apiProjectDTO.setSort(calProjectSort(tableData, moduleName));
        return apiProjectDTO;
    }


    private Integer calProjectSort(YApiProjectTableData yApiProjectTableData, String moduleName) {
        //默认99 排在最后面
        Integer sort = 99;
        TreePathBO scope;
        if (StringUtils.isNotBlank(moduleName) && Objects.nonNull(scope = yApiProjectTableData.getScope()) && scope.isNotNull()) {
            //当前项目配置有指定module生效
            return scope.matchModule(moduleName);
        }
        return sort;
    }
}
