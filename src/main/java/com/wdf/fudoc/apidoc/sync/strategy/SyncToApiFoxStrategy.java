package com.wdf.fudoc.apidoc.sync.strategy;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.pojo.data.FuDocItemData;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.ProjectSyncApiRecordData;
import com.wdf.fudoc.apidoc.sync.dto.SyncApiResultDTO;

import java.util.List;

/**
 * ApiFox同步文档策略
 *
 * @author wangdingfu
 * @date 2023-06-12 21:39:05
 */
public class SyncToApiFoxStrategy extends AbstractSyncApiStrategy {


    @Override
    protected boolean checkConfig(BaseSyncConfigData configData) {
        return true;
    }

    /**
     * 同步api至ApiFox系统
     *
     * @param fuDocItemDataList 接口文档api集合
     * @param configData        配置数据
     * @param apiProjectDTO     同步项目
     * @param projectRecord     同步记录
     * @return 同步结果
     */
    @Override
    protected List<SyncApiResultDTO> doSyncApi(List<FuDocItemData> fuDocItemDataList, BaseSyncConfigData configData, ApiProjectDTO apiProjectDTO, ProjectSyncApiRecordData projectRecord) {

        return null;
    }


    /**
     * 确认需要同步的分类
     *
     * @param apiProjectDTO 同步的项目
     * @param configData    配置数据
     * @param psiClass      当前api所在的java类
     * @param projectRecord 同步记录
     * @return api同步至哪个分类下
     */
    @Override
    protected ApiProjectDTO confirmApiCategory(ApiProjectDTO apiProjectDTO, BaseSyncConfigData configData, PsiClass psiClass, ProjectSyncApiRecordData projectRecord) {
        return null;
    }
}
