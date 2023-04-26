package com.wdf.fudoc.request.execute;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.wdf.fudoc.request.constants.enumtype.ResponseType;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;

import java.util.Objects;


/**
 * http响应数据构建器
 *
 * @author wangdingfu
 * @date 2022-09-23 18:39:38
 */
public class FuHttpResponseBuilder {


    /**
     * 构建请求成功的响应对象
     *
     * @param fuHttpRequestData http请求数据对象
     * @param httpResponse      http响应数据
     */
    public static void buildSuccessResponse(FuHttpRequestData fuHttpRequestData, HttpResponse httpResponse) {
        FuResponseData response = ifNecessaryCreateResponse(fuHttpRequestData);
        response.setHttpResponse(httpResponse);
        response.setBody(httpResponse.bodyBytes());
        response.setStatus(httpResponse.getStatus());
        fuHttpRequestData.setHttpCode(httpResponse.getStatus());
        response.setHeaders(httpResponse.headers());
        response.setContentLength(httpResponse.contentLength());
        response.setResponseType(ResponseType.SUCCESS);
        response.setContent(HttpUtil.getString(response.getBody(), CharsetUtil.CHARSET_UTF_8, null == response.getCharsetFromResponse()));
    }


    /**
     * 构建请求失败的响应数据
     *
     * @param fuHttpRequestData http请求数据对象
     */
    public static void buildErrorResponse(FuHttpRequestData fuHttpRequestData) {
        buildErrorResponse(ResponseType.ERR_UNKNOWN, fuHttpRequestData);
    }

    public static void buildRefusedConnection(FuHttpRequestData fuHttpRequestData) {
        buildErrorResponse(ResponseType.ERR_CONNECTION_REFUSED, fuHttpRequestData);
    }

    public static void buildErrorResponse(ResponseType responseType, FuHttpRequestData fuHttpRequestData) {
        FuResponseData fuResponseData = ifNecessaryCreateResponse(fuHttpRequestData);
        fuResponseData.setResponseType(responseType);
    }


    public static FuResponseData ifNecessaryCreateResponse(FuHttpRequestData fuHttpRequestData) {
        FuResponseData response = fuHttpRequestData.getResponse();
        if (Objects.isNull(response)) {
            response = new FuResponseData();
            fuHttpRequestData.setResponse(response);
        }
        return response;
    }

}
