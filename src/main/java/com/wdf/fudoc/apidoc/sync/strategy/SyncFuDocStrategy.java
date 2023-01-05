package com.wdf.fudoc.apidoc.sync.strategy;

import com.intellij.psi.PsiClass;
import com.wdf.fudoc.apidoc.pojo.context.FuDocContext;
import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiStructureTreeDTO;


/**
 * 将[Fu Doc]生成的接口文档同步至第三方文档系统中
 *
 * @author wangdingfu
 * @date 2022-12-31 15:51:27
 */
public interface SyncFuDocStrategy {


    /**
     * 获取三方接口文档系统接口结构树（分组-->项目--->分类）
     *
     * @param psiClass 同步的接口所属的class对象
     * @return 接口的层级结构
     */
    ApiStructureTreeDTO getApiStructureTree(PsiClass psiClass);

    /**
     * 同步接口文档到第三方文档系统中
     *
     * @param fuDocContext 上下文对象
     * @param psiClass     同步的接口所属的class对象
     */
    void syncFuDoc(FuDocContext fuDocContext, PsiClass psiClass, BaseSyncConfigData baseSyncConfigData);

    /**
     * 创建分类
     *
     * @param apiProjectDTO 分类所属的项目信息
     * @return 分类对象
     */
    ApiCategoryDTO createCategory(ApiProjectDTO apiProjectDTO);


}
