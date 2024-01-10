package com.wdf.fudoc.apidoc.sync.service;

import cn.hutool.core.text.StrFormatter;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.wdf.fudoc.apidoc.sync.data.ApiFoxConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ApiFoxDTO;
import com.wdf.fudoc.apidoc.sync.dto.ApiFoxResult;
import com.wdf.fudoc.apidoc.sync.dto.ApiProjectDTO;
import com.wdf.fudoc.common.exception.FuDocException;
import com.wdf.api.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-06-13 17:25:44
 */
@Slf4j
public class ApiFoxServiceImpl implements ApiFoxService {

    private final static String SYNC_API = "/api/v1/projects/{}/import-data";

    @Override
    public String syncApi(ApiFoxDTO apiFoxDTO, ApiProjectDTO apiProjectDTO, ApiFoxConfigData apiFoxConfigData) {
        String baseUrl = apiFoxConfigData.getBaseUrl();
        String url = baseUrl + StrFormatter.format(SYNC_API, apiProjectDTO.getProjectId());
        try {
            HttpRequest httpRequest = HttpUtil.createPost(url);
            httpRequest.header("Authorization", "Bearer " + apiFoxConfigData.getToken());
            httpRequest.header("X-Apifox-Version", "2022-11-16");
            httpRequest.body(JsonUtil.toJson(apiFoxDTO));
            String postResult = httpRequest.execute().body();
            log.info("同步结果:{}", postResult);
            ApiFoxResult result;
            if (FuStringUtils.isBlank(postResult) || Objects.isNull(result = JsonUtil.toBean(postResult, ApiFoxResult.class))) {
                return "ApiFox返回结果为空";
            }
            Boolean success = result.getSuccess();
            if (Objects.nonNull(success) && success) {
                return FuStringUtils.EMPTY;
            }
            String errorMessage = result.getErrorMessage();
            return FuStringUtils.isBlank(errorMessage) ? "同步异常" : errorMessage;
        } catch (Exception e) {
            log.error("同步Api到ApiFox系统异常", e);
        }
        return "同步失败";
    }
}
