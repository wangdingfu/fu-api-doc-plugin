package com.wdf.fudoc.request.tab.request;

import com.google.common.collect.Lists;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuTableDisableListener;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import com.wdf.fudoc.request.view.FuRequestStatusInfoView;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * 响应头面板
 *
 * @author wangdingfu
 * @date 2023-01-31 10:43:35
 */
public class ResponseHeaderTabView implements FuTab, HttpCallback {

    /**
     * table组件
     */
    private final FuTableComponent<KeyValueTableBO> fuTableComponent;


    /**
     * 状态信息面板
     */
    private final FuRequestStatusInfoView fuRequestStatusInfoView;

    public ResponseHeaderTabView() {
        this.fuRequestStatusInfoView = new FuRequestStatusInfoView();
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class);
        this.fuTableComponent.addListener(new FuTableDisableListener<>());
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Header", null, fuTableComponent.createMainPanel()).builder(this.fuRequestStatusInfoView.getRootPanel());
    }


    /**
     * 初始化请求头数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        FuResponseData response = httpRequestData.getResponse();
        if (Objects.nonNull(response)) {
            Map<String, List<String>> headers = response.getHeaders();
        }
        //设置响应信息
        fuRequestStatusInfoView.initData(httpRequestData);
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        FuRequestData request = fuHttpRequestData.getRequest();
        if (Objects.nonNull(request)) {
            request.setHeaders(fuTableComponent.getDataList());
        }
    }

}
