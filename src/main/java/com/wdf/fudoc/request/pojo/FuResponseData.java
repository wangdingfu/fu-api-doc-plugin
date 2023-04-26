package com.wdf.fudoc.request.pojo;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.wdf.fudoc.request.constants.enumtype.ResponseType;
import lombok.Getter;
import lombok.Setter;

import java.nio.charset.Charset;
import java.util.List;
import java.util.Map;

/**
 * http请求响应结果
 *
 * @author wangdingfu
 * @date 2022-09-18 13:53:12
 */
@Getter
@Setter
public class FuResponseData {

    /**
     * http响应对象
     */
    @JsonIgnore
    private HttpResponse httpResponse;

    /**
     * 响应类型
     */
    private ResponseType responseType;
    /**
     * 响应内容
     */
    private String content;

    /**
     * 字节流内容
     */
    @JsonIgnore
    private byte[] body;

    /**
     * 响应状态码
     */
    @JsonIgnore
    private Integer status;

    /**
     * 请求失败详情
     */
    private String errorDetail;

    /**
     * 响应头
     */
    private Map<String, List<String>> headers;

    /**
     * 内容长度
     */
    private long contentLength;

    /**
     * 文件名
     */
    private String fileName;

    /**
     * 文件路径
     */
    private String filePath;

    /**
     * 响应结果编码
     */
    private Charset charsetFromResponse;
}
