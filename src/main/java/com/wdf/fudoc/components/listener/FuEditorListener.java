package com.wdf.fudoc.components.listener;

/**
 * @author wangdingfu
 * @date 2022-09-19 13:48:10
 */
public interface FuEditorListener {

    /**
     * 内容变更事件
     *
     * @param content 变更后的内容
     */
    default void contentChange(String content) {

    }
}
