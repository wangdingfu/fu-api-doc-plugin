package com.wdf.fudoc.request.pojo;

import cn.hutool.core.util.URLUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

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
     * 接口请求地址
     */
    private String baseUrl;

    /**
     * 请求路径上的参数拼接
     */
    private String paramUrl;

    /**
     * 是否有文件上传
     */
    private boolean isFile;

    /**
     * 请求头
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<KeyValueTableBO> headers;

    /**
     * 请求参数（GET请求）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<KeyValueTableBO> params;

    /**
     * 接口路径上的请求参数（GET请求 POST请求都可能有值）
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<KeyValueTableBO> pathVariables;

    /**
     * 请求body内容(POST请求参数)
     */
    private FuRequestBodyData body;

    /**
     * 获取一个完整的请求地址
     */
    public String getRequestUrl() {
        String params = StringUtils.isNotBlank(this.paramUrl) ? "?" + this.paramUrl : StringUtils.EMPTY;
        if(StringUtils.isBlank(this.baseUrl) && StringUtils.isBlank(this.paramUrl)){
            return StringUtils.EMPTY;
        }
        return URLUtil.normalize(this.baseUrl + params, false, true);
    }

}
