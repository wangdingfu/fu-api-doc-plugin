package com.wdf.fudoc.request;

import com.wdf.fudoc.request.pojo.FuHttpRequestData;

/**
 * @author wangdingfu
 * @date 2022-09-19 22:29:35
 */
public interface HttpCallback {

    /**
     * 初始化数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    void initData(FuHttpRequestData httpRequestData);


    /**
     * 发起请求前置方法
     *
     * @param fuHttpRequestData 请求数据对象
     */
    default void doSendBefore(FuHttpRequestData fuHttpRequestData) {
    }


    /**
     * 发起请求后置方法
     *
     * @param fuHttpRequestData 请求数据对象
     */
    default void doSendAfter(FuHttpRequestData fuHttpRequestData) {
    }
}
