package com.wdf.fudoc.common;

/**
 * 公共的回调类
 *
 * @author wangdingfu
 * @date 2022-08-20 16:54:26
 */
public interface CallbackAction<T> {

    /**
     * 获取数据
     */
    T getData();

    /**
     * 设置数据
     */
    void setData(T data);
}
