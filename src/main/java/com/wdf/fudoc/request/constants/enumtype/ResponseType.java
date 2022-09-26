package com.wdf.fudoc.request.constants.enumtype;

/**
 * 响应类型
 *
 * @author wangdingfu
 * @date 2022-09-26 10:58:35
 */
public enum ResponseType {

    /**
     * 拒绝连接
     */
    ERR_CONNECTION_REFUSED,

    /**
     * 未知错误
     */
    ERR_UNKNOWN,

    /**
     * 超时
     */
    TIME_OUT,

    /**
     * 响应成功
     */
    SUCCESS;
}
