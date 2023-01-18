package com.wdf.fudoc.components.listener;

/**
 * @author wangdingfu
 * @date 2023-01-08 20:24:29
 */
public interface FuViewListener {

    /**
     * 保存数据
     */
    void apply();


    /**
     * 初始化数据（重置数据）
     */
    void reset();
}
