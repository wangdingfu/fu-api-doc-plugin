package com.wdf.fudoc.apidoc.sync.dto;

import lombok.Getter;
import lombok.Setter;

/**
 * YApi公共返回结果对象
 *
 * @author wangdingfu
 * @date 2023-01-06 01:16:37
 */
@Getter
@Setter
public class YApiBaseRes {

    /**
     * 错误码
     */
    private int errcode;

    /**
     * 错误消息
     */
    private String errmsg;

    /**
     * 响应数据
     */
    private Object data;


    public boolean success() {
        return this.errcode == 0;
    }
}
