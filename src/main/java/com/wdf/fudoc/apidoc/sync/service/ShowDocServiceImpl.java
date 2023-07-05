package com.wdf.fudoc.apidoc.sync.service;

import cn.hutool.core.util.URLUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import com.wdf.fudoc.apidoc.sync.data.ShowDocConfigData;
import com.wdf.fudoc.apidoc.sync.dto.ShowDocDTO;
import com.wdf.fudoc.apidoc.sync.dto.ShowDocResult;
import com.wdf.fudoc.common.constant.UrlConstants;
import com.wdf.fudoc.util.JsonUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

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
        String sync_api_url = StringUtils.equals(baseUrl, UrlConstants.SHOW_DOC)
                ? UrlConstants.SHOW_DOC + UrlConstants.SHOW_DOC_SYNC_API
                : URLUtil.completeUrl(baseUrl, UrlConstants.SHOW_DOC_PRIVATE_SYNC_API);
        HttpRequest httpRequest = HttpUtil.createPost(baseUrl + sync_api_url);
        httpRequest.body(JsonUtil.toJson(showDocDTO));
        try {
            String body = httpRequest.execute().body();
            ShowDocResult showDocResult;
            if (StringUtils.isBlank(body) || Objects.isNull(showDocResult = JsonUtil.toBean(body, ShowDocResult.class))) {
                return "ShowDoc返回结果异常";
            }
            if ("0".equals(showDocResult.getCode())) {
                return StringUtils.EMPTY;
            }
            String message = showDocResult.getMessage();
            return StringUtils.isBlank(message) ? "同步文档至ShowDoc异常" : message;
        } catch (Exception e) {
            log.error("同步文档至ShowDoc异常", e);
            return "同步文档至ShowDoc异常";
        }
    }
}
