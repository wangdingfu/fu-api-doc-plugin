package com.wdf.fudoc.apidoc.sync.data;

import com.intellij.openapi.module.Module;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.ProjectSyncApiRecordData;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;
import com.wdf.api.util.ProjectUtils;
import lombok.Getter;
import lombok.Setter;
import com.wdf.fudoc.util.FuStringUtils;

import java.io.Serializable;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 同步到第三方接口文档系统抽象配置
 *
 * @author wangdingfu
 * @date 2022-12-31 22:21:30
 */
@Getter
@Setter
public abstract class BaseSyncConfigData implements Serializable {

    /**
     * 三方接口文档系统地址
     */
    protected String baseUrl;

    /**
     * 自动生成分类
     */
    protected boolean autoGenCategory;

    /**
     * 所有项目下接口的同步记录
     * key:项目路径 value：项目同步记录
     */
    @Getter
    private Map<String, ProjectSyncApiRecordData> projectSyncRecordMap = new ConcurrentHashMap<>();

    public ProjectSyncApiRecordData getProjectRecord(String projectPath, String projectName) {
        if (FuStringUtils.isNotBlank(projectPath) && FuStringUtils.isNotBlank(projectName)) {
            return projectSyncRecordMap.get(projectPath + "-" + projectName);
        }
        return null;
    }

    public void addProjectRecordData(String projectPath, String projectName, ProjectSyncApiRecordData recordData) {
        if (FuStringUtils.isBlank(projectPath) || FuStringUtils.isBlank(projectName) || Objects.isNull(recordData)) {
            return;
        }
        projectSyncRecordMap.put(projectPath + "-" + projectName, recordData);
    }

    /**
     * 清除缓存数据
     *
     * @param isAll 是否全部清楚
     */
    public void clear(boolean isAll) {
        if (isAll) {
            projectSyncRecordMap.clear();
            clearData(true);
        } else {
            Set<String> keys = projectSyncRecordMap.keySet();
            for (String key : keys) {
                if (key.contains(ProjectUtils.getCurrentProjectPath())) {
                    projectSyncRecordMap.remove(key);
                }
            }
        }
    }


    /**
     * 获取当前module对应到第三方接口文档的项目名称
     *
     * @param module 代码中的java module名称
     * @return 第三方接口文档系统的项目名称
     */
    public abstract List<ApiProjectDTO> getProjectConfigList(Module module);


    /**
     * 同步项目配置数据
     *
     * @param apiProjectDTOList 项目配置数据
     */
    public abstract void syncApiProjectList(Module module, List<ApiProjectDTO> apiProjectDTOList);


    /**
     * 是否存在当前项目配置
     */
    public abstract boolean isExistsConfig();

    /**
     * 清楚数据缓存
     *
     * @param isAll true 全部清除 false 清除当前项目
     */
    public abstract void clearData(boolean isAll);

    /**
     * 获取同步的接口文档系统
     */
    public abstract ApiDocSystem getApiSystem();

    /**
     * 获取接口文档地址
     *
     * @return 第三方接口文档系统中指定文档的地址
     */
    public abstract String getApiDocUrl(SyncApiResultDTO syncApiResultDTO);
}
