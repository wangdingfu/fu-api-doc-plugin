package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.common.base.KeyValueBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-07-14 14:04:16
 */
@Getter
@Setter
public class FuRequestConsoleData {

    /**
     * 请求类型
     */
    private String methodName;

    /**
     * 请求地址
     */
    private String url;

    /**
     * 请求头
     */
    private List<KeyValueBO> headers;

    /**
     * 请求body
     */
    private String requestBody;

}
