package com.wdf.fudoc.request.tab.request;

import com.google.common.collect.Lists;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-06 14:19:59
 */
public class HttpPathParamsTab implements FuTab, HttpCallback {


    private final FuTableComponent<KeyValueTableBO> pathTable;

    public HttpPathParamsTab() {
        this.pathTable = FuTableComponent.create(FuTableColumnFactory.pathVariable(), KeyValueTableBO.class);
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Path", null, this.pathTable.createMainPanel()).builder();
    }

    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        FuRequestData request;
        if (Objects.isNull(httpRequestData) || Objects.isNull(request = httpRequestData.getRequest())) {
            return;
        }
        List<KeyValueTableBO> pathVariables = request.getPathVariables();
        if (CollectionUtils.isNotEmpty(pathVariables)) {
            this.pathTable.setDataList(Lists.newArrayList(pathVariables));
        }
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        FuRequestData request;
        if (Objects.isNull(fuHttpRequestData) || Objects.isNull(request = fuHttpRequestData.getRequest())) {
            return;
        }
        request.setPathVariables(Lists.newArrayList(this.pathTable.getDataList()));
    }
}
