package com.wdf.fudoc.apidoc.sync.service;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpUtil;
import com.wdf.fudoc.apidoc.sync.dto.ApiCategoryDTO;
import com.wdf.fudoc.apidoc.sync.dto.YApiProjectInfoDTO;
import com.wdf.fudoc.util.YApiUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author wangdingfu
 * @date 2023-01-07 02:46:19
 */
public class YApiServiceImpl implements YApiService {

    String findProjectInfoUrl = "/api/project/get";
    String categoryListUrl = "/api/interface/getCatMenu";

    @Override
    public YApiProjectInfoDTO findProjectInfo(String baseUrl, String token) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("token", token);
        String result = HttpUtil.get(URLUtil.completeUrl(baseUrl, findProjectInfoUrl), paramMap);
        return YApiUtil.getData(result, YApiProjectInfoDTO.class);
    }

    @Override
    public List<ApiCategoryDTO> categoryList(String baseUrl, String projectId, String token) {
        Map<String, Object> paramMap = new HashMap<>();
        paramMap.put("token", token);
        paramMap.put("project_id", projectId);
        String result = HttpUtil.get(URLUtil.completeUrl(baseUrl, categoryListUrl), paramMap);
        return YApiUtil.getDataList(result, ApiCategoryDTO.class);
    }

}
