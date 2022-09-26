package com.wdf.fudoc.request.execute;

import cn.hutool.core.lang.Assert;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-09-23 18:07:16
 */
public class FuHttpRequestBuilder {

    /**
     * http请求数据对象
     */
    private final FuHttpRequestData fuHttpRequestData;

    /**
     * http请求对象
     */
    private final HttpRequest httpRequest;

    public FuHttpRequestBuilder(FuHttpRequestData fuHttpRequestData, HttpRequest httpRequest) {
        this.fuHttpRequestData = fuHttpRequestData;
        this.httpRequest = httpRequest;
        //添加请求头
        this.addHeader(this.httpRequest, this.fuHttpRequestData.getRequest().getHeaders());
        //添加form

        //添加body
    }

    public static FuHttpRequestBuilder getInstance(FuHttpRequestData fuHttpRequestData) {
        FuRequestData request = fuHttpRequestData.getRequest();
        String requestUrl = request.getRequestUrl();
        RequestType requestType = request.getRequestType();
        return new FuHttpRequestBuilder(fuHttpRequestData, createHttpRequest(requestType, requestUrl));
    }

    public HttpRequest builder() {
        return this.httpRequest;
    }


    /**
     * 将请求头信息添加到http请求中
     *
     * @param httpRequest http请求对象
     * @param headers     请求头
     */
    private void addHeader(HttpRequest httpRequest, List<KeyValueTableBO> headers) {
        if (CollectionUtils.isNotEmpty(headers)) {
            headers.stream().filter(KeyValueTableBO::getSelect).forEach(data -> httpRequest.header(data.getKey(), data.getValue()));
        }
    }


    private static HttpRequest createHttpRequest(RequestType requestType, String requestUrl) {
        if (Objects.nonNull(requestType)) {
            switch (requestType) {
                case GET:
                    return HttpUtil.createGet(requestUrl);
                case POST:
                    return HttpUtil.createPost(requestUrl);
                case DELETE:
                    return HttpUtil.createRequest(Method.DELETE, requestUrl);
            }
        }
        return HttpUtil.createGet(requestUrl);
    }

}
