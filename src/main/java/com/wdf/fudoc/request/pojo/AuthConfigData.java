package com.wdf.fudoc.request.pojo;

import com.wdf.fudoc.components.bo.BaseTemplate;
import lombok.Getter;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2023-02-21 00:03:23
 */
@Getter
@Setter
public class AuthConfigData extends BaseTemplate {

    /**
     * 鉴权请求数据对象
     */
    private FuHttpRequestData httpRequestData;
}
