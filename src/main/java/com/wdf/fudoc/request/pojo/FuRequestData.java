package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * http请求数据
 *
 * @author wangdingfu
 * @date 2022-09-18 13:52:19
 */
@Getter
@Setter
public class FuRequestData {

    /**
     * 请求类型
     */
    private RequestType requestType;

    /**
     * 完整的请求地址 包括域名
     */
    private String requestUrl;

    /**
     * 接口请求地址
     */
    private String apiUrl;

    /**
     * 请求头
     */
    private List<KeyValueTableBO> headers;

    /**
     * 请求参数（GET请求）
     */
    private List<KeyValueTableBO> params;

    /**
     * 接口路径上的请求参数（GET请求 POST请求都可能有值）
     */
    private List<KeyValueTableBO> pathVariables;

    /**
     * 请求body内容(POST请求参数)
     */
    private FuRequestBodyData body;

}
