package com.wdf.fudoc.request.execute;

import cn.hutool.core.io.FileUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpUtil;
import cn.hutool.http.Method;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-09-23 18:07:16
 */
public class FuHttpRequestBuilder {

    /**
     * http请求对象
     */
    private final HttpRequest httpRequest;

    public FuHttpRequestBuilder(FuHttpRequestData fuHttpRequestData, HttpRequest httpRequest) {
        this.httpRequest = httpRequest;
        FuRequestData request = fuHttpRequestData.getRequest();
        //添加请求头
        this.addHeader(this.httpRequest, request.getHeaders());
        FuRequestBodyData body = request.getBody();
        if (Objects.isNull(body)) {
            body = new FuRequestBodyData();
            request.setBody(body);
        }
        //添加GET请求
        addForm(request.getParams(), false);
        //添加form-data
        addForm(body.getFormDataList(), true);
        //添加x-www-form-urlencoded
        addForm(body.getFormUrlEncodedList(), false);
        //添加raw
        addBody(body.getRaw());
        //添加json
        addBody(body.getJson());
        //添加binary
        addBody(body.getBinary());
    }


    private void addForm(List<KeyValueTableBO> formDataList, boolean isMultiFile) {
        if (CollectionUtils.isNotEmpty(formDataList)) {
            for (KeyValueTableBO keyValueTableBO : formDataList) {
                if(Objects.isNull(keyValueTableBO.getSelect()) || !keyValueTableBO.getSelect()){
                    continue;
                }
                String requestParamType = keyValueTableBO.getRequestParamType();
                if (isMultiFile && RequestParamType.FILE.getCode().equals(requestParamType)) {
                    File file = new File(keyValueTableBO.getValue());
                    byte[] bytes = FileUtil.readBytes(file);
                    this.httpRequest.form(keyValueTableBO.getKey(), bytes, file.getName());
                } else {
                    this.httpRequest.form(keyValueTableBO.getKey(), keyValueTableBO.getValue());
                }
            }
        }
    }


    private void addBody(String content) {
        if (StringUtils.isNotBlank(content)) {
            this.httpRequest.body(content);
        }
    }


    private void addBody(byte[] body) {
        if (Objects.nonNull(body) && body.length > 0) {
            this.httpRequest.body(body);
        }
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
