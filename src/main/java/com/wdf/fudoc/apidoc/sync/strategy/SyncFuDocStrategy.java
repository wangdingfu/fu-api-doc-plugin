package com.wdf.fudoc.apidoc.sync.strategy;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.config.state.FuDocSetting;
import com.wdf.fudoc.apidoc.config.state.FuDocSyncSetting;
import com.wdf.fudoc.apidoc.constant.enumtype.ApiDocSystem;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.AddApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiStructureTreeDTO;
import com.wdf.fudoc.common.ServiceHelper;

import java.util.List;


/**
 * 将[Fu Doc]生成的接口文档同步至第三方文档系统中
 *
 * @author wangdingfu
 * @date 2022-12-31 15:51:27
 */
public interface SyncFuDocStrategy {

    /**
     * 同步接口文档到第三方文档系统中
     *
     * @param fuDocContext 上下文对象
     * @param psiClass     同步的接口所属的class对象
     */
    void syncFuDoc(FuDocContext fuDocContext, PsiClass psiClass, BaseSyncConfigData configData);

    /**
     * 创建分类
     *
     * @return 分类对象
     */
    ApiCategoryDTO createCategory(BaseSyncConfigData configData, ApiProjectDTO apiProjectDTO, String categoryName);


    /**
     * 分类集合
     *
     * @param apiProjectDTO 项目名称
     * @param configData    配置数据
     * @return 第三方接口文档系统的接口分类集合
     */
    List<ApiCategoryDTO> categoryList(ApiProjectDTO apiProjectDTO, BaseSyncConfigData configData);


}
