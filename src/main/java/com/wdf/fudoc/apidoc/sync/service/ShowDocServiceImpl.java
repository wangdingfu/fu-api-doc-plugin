package com.wdf.fudoc.apidoc.sync.service;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.wdf.fudoc.apidoc.sync.data.ShowDocConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ShowDocDTO;
import com.wdf.fudoc.apidoc.sync.dto.ShowDocResult;
import com.wdf.api.constants.UrlConstants;
import com.wdf.api.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-05 20:13:58
 */
@Slf4j
public class ShowDocServiceImpl implements ShowDocService {


    @Override
    public String syncApi(ShowDocDTO showDocDTO, ShowDocConfigData showDocConfigData) {
        String baseUrl = showDocConfigData.getBaseUrl();
        String syncApiUrl = FuStringUtils.equals(baseUrl, UrlConstants.SHOW_DOC)
                ? UrlConstants.SHOW_DOC + UrlConstants.SHOW_DOC_DEFAULT
                : URLUtil.completeUrl(baseUrl, UrlConstants.SHOW_DOC_PRIVATE_SYNC_API);
        HttpRequest httpRequest = HttpUtil.createPost(syncApiUrl);
        httpRequest.body(JsonUtil.toJson(showDocDTO));
        try {
            String body = httpRequest.execute().body();
            ShowDocResult showDocResult;
            if (FuStringUtils.isBlank(body) || Objects.isNull(showDocResult = JsonUtil.toBean(body, ShowDocResult.class))) {
                return "ShowDoc返回结果异常";
            }
            if ("0".equals(showDocResult.getCode())) {
                return FuStringUtils.EMPTY;
            }
            String message = showDocResult.getMessage();
            return FuStringUtils.isBlank(message) ? "同步文档至ShowDoc异常" : message;
        } catch (Exception e) {
            log.error("同步文档至ShowDoc异常", e);
            return "同步文档至ShowDoc异常";
        }
    }
}
