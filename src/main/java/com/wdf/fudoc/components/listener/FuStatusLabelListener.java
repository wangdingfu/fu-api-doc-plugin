package com.wdf.fudoc.components.listener;

import com.wdf.fudoc.request.pojo.BasePopupMenuItem;

import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-07-09 22:59:37
 */
public interface FuStatusLabelListener {

    /**
     * 获取需要弹框展示的列表集合
     */
    List<BasePopupMenuItem> getList();


    void select(String text);
}
