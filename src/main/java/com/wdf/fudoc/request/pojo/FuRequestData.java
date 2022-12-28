package com.wdf.fudoc.request.pojo;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.URLUtil;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.request.data.FuRequestSettingData;
import com.wdf.fudoc.request.state.FuRequestSettingState;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import com.wdf.fudoc.util.ObjectUtils;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;
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

    /**
     * 获取一个完整的请求地址
     */
    public String getRequestUrl() {
        String params = StringUtils.isNotBlank(this.paramUrl) ? "?" + this.paramUrl : StringUtils.EMPTY;
        return URLUtil.normalize(this.baseUrl + params, false, true);
    }

}
