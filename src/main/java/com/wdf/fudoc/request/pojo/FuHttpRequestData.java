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
     * 接口唯一标识
     */
    private String apiKey;

    /**
     * 请求id 标识唯一一次请求
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

    /**
     * 请求状态 true 正在请求中
     */
    private boolean requestStatus;

    /**
     * 请求的时间 单位:ms
     */
    private Long time;

    /**
     * http状态码
     */
    private Integer httpCode;
}
