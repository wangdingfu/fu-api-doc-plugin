package com.wdf.fudoc.request.pojo;

import cn.hutool.core.util.URLUtil;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import com.wdf.fudoc.apidoc.constant.enumtype.ContentType;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.components.bo.HeaderKeyValueBO;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.spring.SpringConfigManager;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;

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
     * 域名地址
     */
    private String domain;
    /**
     * server.servlet.context-path配置项目前缀
     */
    private String contextPath;
    /**
     * 接口请求地址
     */
    private String baseUrl;

    /**
     * 请求路径上的参数拼接
     */
    private String paramUrl;

    private String requestUrl;

    /**
     * 是否有文件上传
     */
    private boolean isFile;

    /**
     * 请求头
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    private List<HeaderKeyValueBO> headers;

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


    public void addHeader(String key, String value) {
        if (Objects.isNull(this.headers)) {
            this.headers = Lists.newArrayList();
        }
        HeaderKeyValueBO keyValueTableBO = this.headers.stream().filter(f -> StringUtils.isNotBlank(f.getKey())).filter(f -> f.getKey().equals(key)).findFirst().orElse(null);
        if (Objects.isNull(keyValueTableBO)) {
            keyValueTableBO = new HeaderKeyValueBO(true, key, value);
            this.headers.add(keyValueTableBO);
        }
        keyValueTableBO.setValue(value);
    }

    public void addContentType(ContentType contentType) {
        if (Objects.isNull(contentType)) {
            removeHeader(FuDocConstants.CONTENT_TYPE);
            return;
        }
        addHeader(FuDocConstants.CONTENT_TYPE, contentType.getType());
    }

    public void removeHeader(String key) {
        if (CollectionUtils.isNotEmpty(this.headers)) {
            this.headers.removeIf(f -> StringUtils.isBlank(f.getKey()) || f.getKey().equals(key));
        }
    }

    public String getRequestUrl() {
        return getRequestUrl(this.baseUrl);
    }

    /**
     * 获取一个完整的请求地址
     */
    public String getRequestUrl(String baseUrl) {
        if (StringUtils.isNotBlank(this.requestUrl)) {
            return this.requestUrl;
        }
        if (StringUtils.isBlank(this.domain)) {
            return StringUtils.EMPTY;
        }
        String params = StringUtils.isNotBlank(this.paramUrl) ? "?" + this.paramUrl : StringUtils.EMPTY;
        if (Objects.isNull(baseUrl)) {
            baseUrl = StringUtils.EMPTY;
        }
        String contextPathUrl = StringUtils.isBlank(this.contextPath) ? StringUtils.EMPTY : this.contextPath;
        return URLUtil.normalize(this.domain + contextPathUrl + baseUrl + params, false, true);
    }


}
