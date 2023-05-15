package com.wdf.fudoc.storage.handler;

import com.wdf.fudoc.storage.enumtype.FuStorageType;

/**
 * @author wangdingfu
 * @date 2023-05-15 12:58:24
 */
public interface FuStorageHandler<T> {

    /**
     * 获取当前持久化那种类型数据
     */
    FuStorageType getType();

    /**
     * 加载数据
     */
    T loadData();

    /**
     * 持久化数据到硬盘中
     *
     * @param data 数据对象
     */
    void saveData(T data);


}
