package com.wdf.fudoc.request;

import com.wdf.fudoc.request.pojo.FuHttpRequestData;

/**
 * @author wangdingfu
 * @date 2022-09-18 22:31:26
 */
public interface InitRequestData {

    /**
     * 初始化数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    void initData(FuHttpRequestData httpRequestData);
}
