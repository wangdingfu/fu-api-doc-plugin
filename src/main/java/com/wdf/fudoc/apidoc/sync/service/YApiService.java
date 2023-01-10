package com.wdf.fudoc.apidoc.sync.service;

import com.wdf.fudoc.apidoc.sync.dto.*;

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
     * 创建分类
     *
     * @param baseUrl               yapi域名
     * @param yApiCreateCategoryDTO 创建分类需要的参数
     * @return 创建成功的分类
     */
    ApiCategoryDTO createCategory(String baseUrl, YApiCreateCategoryDTO yApiCreateCategoryDTO);

    /**
     * 获取接口分类集合
     *
     * @param baseUrl   yapi域名
     * @param projectId 项目id
     * @param token     项目token
     * @return 分类集合
     */
    List<ApiCategoryDTO> categoryList(String baseUrl, String projectId, String token);


    /**
     * 保存或更新接口
     *
     * @param yApiSaveDTO 接口请求参数
     * @return 响应结果
     */
    boolean saveOrUpdate(String baseUrl, YApiSaveDTO yApiSaveDTO);
}
