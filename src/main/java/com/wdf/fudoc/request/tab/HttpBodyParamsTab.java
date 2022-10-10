package com.wdf.fudoc.request.tab;

import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.InitRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;

/**
 * @author wangdingfu
 * @date 2022-09-18 22:54:24
 */
public class HttpBodyParamsTab  implements FuTab, HttpCallback {


    @Override
    public TabInfo getTabInfo() {
        return null;
    }

    @Override
    public void initData(FuHttpRequestData httpRequestData) {

    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {

    }
}
