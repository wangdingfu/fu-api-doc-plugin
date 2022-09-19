package com.wdf.fudoc.request.execute;


/**
 * @author wangdingfu
 * @date 2022-09-19 20:23:30
 */
public interface FuHttpRequest {

    /**
     * 获取请求状态
     */
    boolean getStatus();

    /**
     * 发起请求
     */
    void doSend();

    /**
     * 请求完毕
     */
    void finished();


    /**
     * 停止请求
     */
    void doStop();
}
