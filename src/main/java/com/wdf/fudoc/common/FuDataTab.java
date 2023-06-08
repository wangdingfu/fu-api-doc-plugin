package com.wdf.fudoc.common;

/**
 * @author wangdingfu
 * @date 2023-06-08 22:39:12
 */
public interface FuDataTab<T> extends FuTab {

    void initData(T data);

    void saveData(T data);
}
