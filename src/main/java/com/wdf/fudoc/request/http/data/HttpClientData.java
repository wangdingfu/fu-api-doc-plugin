package com.wdf.fudoc.request.http.data;

import com.wdf.fudoc.common.base.KeyValueBO;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

/**
 * 映射.http文件的对象
 * @author wangdingfu
 * @date 2023-05-19 22:27:05
 */
@Getter
@Setter
public class HttpClientData {

    /**
     * 接口名称
     */
    private String apiName;
    /**
     * 请求方法
     */
    private String methodName;
    /**
     * 请求url
     */
    private String url;

    /**
     * 请求头
     */
    private List<KeyValueBO> headers;

    /**
     * 请求体
     */
    private String requestBody;

    /**
     * 响应文件路径集合
     */
    private List<String> responseFiles;
}
