package com.wdf.fudoc.request.pojo;

import lombok.Getter;
import lombok.Setter;

/**
 * api接口发起http请求数据对象
 *
 * @author wangdingfu
 * @date 2022-09-18 13:52:46
 */
@Getter
@Setter
public class FuHttpRequestData {

    /**
     * 接口ID
     */
    private String apiId;

    /**
     * 请求id
     */
    private Integer requestId;

    /**
     * 当前接口所属的module
     */
    private String moduleId;

    /**
     * 接口名称
     */
    private String apiName;

    /**
     * 请求数据
     */
    private FuRequestData request;

    /**
     * 响应数据
     */
    private FuResponseData response;
}
