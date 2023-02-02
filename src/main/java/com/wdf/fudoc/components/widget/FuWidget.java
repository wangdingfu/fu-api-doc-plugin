package com.wdf.fudoc.components.widget;

import com.wdf.fudoc.request.pojo.FuHttpRequestData;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2023-02-01 10:45:04
 */
public interface FuWidget {

    JComponent getComponent();

    void initData(FuHttpRequestData fuHttpRequestData);
}