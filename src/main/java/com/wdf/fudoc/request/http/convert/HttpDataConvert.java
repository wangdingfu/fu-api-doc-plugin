package com.wdf.fudoc.request.http.convert;

import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.ContentType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.common.base.KeyValueBO;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.request.http.data.HttpClientData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.util.ObjectUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
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
        httpClientData.setHeaders(ObjectUtils.listToList(request.getHeaders(), data -> new KeyValueBO(data.getKey(), data.getValue())));
        httpClientData.setRequestBody(buildRequestBody(request));
        return httpClientData;
    }





    public static FuHttpRequestData convert(HttpClientData httpClientData){
        FuHttpRequestData fuHttpRequestData = new FuHttpRequestData();
        fuHttpRequestData.setApiName(httpClientData.getApiName());
        FuRequestData request = new FuRequestData();
        request.setRequestType(RequestType.getRequestType(httpClientData.getMethodName()));
        String url = httpClientData.getUrl();
        request.setBaseUrl(StringUtils.substringBefore(url,"?"));
        request.setParamUrl(StringUtils.substringAfter(url,"?"));
        request.setHeaders(ObjectUtils.listToList(httpClientData.getHeaders(),data->new KeyValueTableBO(true,data.getKey(),data.getValue())));
        fuHttpRequestData.setRequest(request);

        return fuHttpRequestData;
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
        List<KeyValueTableBO> headers = fuRequestData.getHeaders();
        String contentType = headers.stream().filter(f -> f.getKey().equals(FuDocConstants.CONTENT_TYPE)).findFirst().map(KeyValueTableBO::getValue).orElse(null);
        if (StringUtils.isBlank(contentType)) {
            if (StringUtils.isNotBlank(body.getJson())) {
                return body.getJson();
            }
            if (StringUtils.isNotBlank(body.getRaw())) {
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


    public static String toParams(List<KeyValueTableBO> paramList) {
        if (CollectionUtils.isNotEmpty(paramList)) {
            return paramList.stream().map(m -> m.getKey() + "=" + m.getValue()).collect(Collectors.joining("&"));
        }
        return StringUtils.EMPTY;
    }



}
