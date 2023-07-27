package com.wdf.fudoc.request.manager;

import com.intellij.httpClient.converters.RequestBuilder;
import com.intellij.httpClient.converters.curl.CurlRequestBuilder;
import com.intellij.httpClient.http.request.*;
import com.intellij.httpClient.http.request.psi.HttpRequest;
import com.intellij.httpClient.http.request.run.HttpRequestValidationException;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.common.FuDocRender;
import com.wdf.fudoc.request.http.convert.HttpDataConvert;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-27 21:22:52
 */
public class FuCurlManager {


    public static String toCurl(Project project, FuHttpRequestData requestData) {
        if (Objects.isNull(requestData)) {
            return StringUtils.EMPTY;
        }
        HttpRequestPsiFile psiFile = HttpRequestPsiFactory.createDummyFile(project, FuDocRender.httpRender(HttpDataConvert.convert(requestData)));
        HttpRequest newRequest = HttpRequestPsiUtils.getFirstRequest(psiFile);
        if (Objects.isNull(newRequest)) {
            return StringUtils.EMPTY;
        }
        try {
            CurlRequestBuilder curlRequestBuilder = new CurlRequestBuilder();
            return (String) HttpRequestPsiConverter.convertFromHttpRequest(newRequest, HttpRequestVariableSubstitutor.getDefault(project), (RequestBuilder) curlRequestBuilder);
        } catch (HttpRequestValidationException e) {
            return StringUtils.EMPTY;
        }
    }


}
