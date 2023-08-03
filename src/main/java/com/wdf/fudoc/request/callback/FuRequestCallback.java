package com.wdf.fudoc.request.callback;

import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.psi.PsiElement;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.tab.request.RequestTabView;

/**
 * @author wangdingfu
 * @date 2023-06-12 22:15:44
 */
public interface FuRequestCallback {
    /**
     * 停止正在发起的http请求
     */
    void stopHttp();


    boolean getSendStatus();

    void initData(FuHttpRequestData fuHttpRequestData);


    PsiElement getPsiElement();

    RequestTabView getRequestTabView();


    default boolean isWindow() {
        return false;
    }


    default void refresh() {

    }
}
