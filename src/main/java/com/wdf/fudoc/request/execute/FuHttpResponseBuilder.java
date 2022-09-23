package com.wdf.fudoc.request.execute;

import cn.hutool.http.HttpResponse;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;

import java.util.Objects;


/**
 * @author wangdingfu
 * @date 2022-09-23 18:39:38
 */
public class FuHttpResponseBuilder {


    public static void buildResponse(FuHttpRequestData fuHttpRequestData, HttpResponse httpResponse) {
        FuResponseData response = fuHttpRequestData.getResponse();
        if (Objects.isNull(response)) {
            response = new FuResponseData();
            fuHttpRequestData.setResponse(response);
        }
        response.setHttpResponse(httpResponse);
        response.setBody(httpResponse.bodyBytes());
        response.setStatus(httpResponse.getStatus());
        response.setHeaders(httpResponse.headers());
        response.setContentLength(httpResponse.contentLength());
    }

}
