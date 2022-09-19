package com.wdf.fudoc.request;

import com.wdf.fudoc.request.execute.FuHttpRequest;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;

/**
 * @author wangdingfu
 * @date 2022-09-19 22:29:35
 */
public interface HttpCallback {


    void callback(FuHttpRequestData fuHttpRequestData);
}
