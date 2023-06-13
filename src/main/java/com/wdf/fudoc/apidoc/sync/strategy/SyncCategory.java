package com.wdf.fudoc.apidoc.sync.strategy;

import com.wdf.fudoc.apidoc.sync.data.BaseSyncConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-06-12 22:53:05
 */
public interface SyncCategory {

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
