package com.wdf.fudoc.request.pojo;

import cn.hutool.core.util.CharsetUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
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
    private byte[] body;

    /**
     * 响应状态码
     */
    private int status;

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
     * 响应结果编码
     */
    private Charset charsetFromResponse;

    public String getContent() {
        return HttpUtil.getString(this.getBody(), CharsetUtil.CHARSET_UTF_8, null == this.charsetFromResponse);
    }

    /**
     * 请求是否成功，判断依据为：状态码范围在200~299内。
     *
     * @return 是否成功请求
     * @since 4.1.9
     */
    public boolean isOk() {
        return this.status >= 200 && this.status < 300;
    }
}
