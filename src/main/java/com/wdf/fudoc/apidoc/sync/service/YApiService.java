package com.wdf.fudoc.apidoc.sync.service;

import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.YApiProjectInfoDTO;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-01-07 02:42:51
 */
public interface YApiService {

    /**
     * 根据项目token获取项目信息
     *
     * @param token 项目token
     * @return 项目基础信息
     */
    YApiProjectInfoDTO findProjectInfo(String baseUrl, String token);


    /**
     * 获取接口分类集合
     *
     * @param baseUrl   yapi域名
     * @param projectId 项目id
     * @param token     项目token
     * @return 分类集合
     */
    List<ApiCategoryDTO> categoryList(String baseUrl, String projectId, String token);
}
