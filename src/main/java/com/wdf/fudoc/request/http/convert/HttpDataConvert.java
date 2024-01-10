package com.wdf.fudoc.request.http.convert;

import com.wdf.fudoc.apidoc.constant.enumtype.ContentType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.common.base.KeyValueBO;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.components.bo.HeaderKeyValueBO;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.request.http.data.HttpClientData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-05-19 22:45:42
 */
public class HttpDataConvert {


    public static HttpClientData convert(FuHttpRequestData fuHttpRequestData) {
        HttpClientData httpClientData = new HttpClientData();
        httpClientData.setApiName(fuHttpRequestData.getApiName());
        FuRequestData request = fuHttpRequestData.getRequest();
        httpClientData.setMethodName(request.getRequestType().getRequestType());
        httpClientData.setUrl(buildUrl(request));
        List<HeaderKeyValueBO> headers = request.getHeaders();
        if (CollectionUtils.isNotEmpty(headers)) {
            httpClientData.setHeaders(headers.stream().filter(HttpDataConvert::isHeader)
                    .map(m -> new KeyValueBO(m.getKey(), m.getValue())).collect(Collectors.toList()));
        }
        httpClientData.setRequestBody(buildRequestBody(request));
        return httpClientData;
    }


    public static String buildUrl(FuRequestData fuRequestData) {
        String requestUrl = fuRequestData.getRequestUrl();
        List<KeyValueTableBO> pathVariables = fuRequestData.getPathVariables();
        if (CollectionUtils.isNotEmpty(pathVariables)) {
            for (KeyValueTableBO pathVariable : pathVariables) {
                requestUrl = requestUrl.replace("{{" + pathVariable.getKey() + "}}", pathVariable.getValue());
            }
        }
        return requestUrl;
    }


    public static String buildRequestBody(FuRequestData fuRequestData) {
        RequestType requestType = fuRequestData.getRequestType();
        if (RequestType.GET.equals(requestType)) {
            //get请求没有requestBody
            return null;
        }
        FuRequestBodyData body = fuRequestData.getBody();
        String contentType = getContentType(fuRequestData.getHeaders());
        if (FuStringUtils.isBlank(contentType)) {
            if (FuStringUtils.isNotBlank(body.getJson())) {
                return body.getJson();
            }
            if (FuStringUtils.isNotBlank(body.getRaw())) {
                return body.getRaw();
            }
            if (CollectionUtils.isNotEmpty(body.getFormUrlEncodedList())) {
                return toParams(body.getFormUrlEncodedList());
            }
        }
        if (ContentType.JSON.getType().equals(contentType)) {
            return body.getJson();
        }
        return toParams(body.getFormUrlEncodedList());
    }

    private static String getContentType(List<HeaderKeyValueBO> headers) {
        if (CollectionUtils.isEmpty(headers)) {
            return null;
        }
        return headers.stream().filter(HttpDataConvert::isHeader).filter(f -> f.getKey().equals(FuDocConstants.CONTENT_TYPE)).findFirst().map(KeyValueTableBO::getValue).orElse(null);
    }

    private static boolean isHeader(HeaderKeyValueBO f) {
        return Objects.nonNull(f) && FuStringUtils.isNotBlank(f.getKey()) && FuStringUtils.isNotBlank(f.getValue());
    }


    public static String toParams(List<KeyValueTableBO> paramList) {
        if (CollectionUtils.isNotEmpty(paramList)) {
            return paramList.stream().map(m -> m.getKey() + "=" + m.getValue()).collect(Collectors.joining("&"));
        }
        return FuStringUtils.EMPTY;
    }


}
