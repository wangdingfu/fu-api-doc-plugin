package com.wdf.fudoc.request.tab;

import com.google.common.collect.Lists;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.InitRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.test.factory.FuTableColumnFactory;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import com.wdf.fudoc.util.FuComponentsUtils;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * http请求头tab页
 *
 * @author wangdingfu
 * @date 2022-09-17 21:30:58
 */
public class HttpHeaderTab implements FuTab, HttpCallback {

    /**
     * table组件
     */
    private final FuTableComponent<KeyValueTableBO> fuTableComponent;

    public HttpHeaderTab() {
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class);
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Header", null, fuTableComponent.createPanel())
                .addBar("Bulk Edit", FuDocIcons.FU_REQUEST_BULK_EDIT, FuComponentsUtils.createEmptyEditor())
                .builder();
    }


    /**
     * 初始化请求头数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        FuRequestData request = httpRequestData.getRequest();
        List<KeyValueTableBO> headers = request.getHeaders();
        if (CollectionUtils.isNotEmpty(headers)) {
            this.fuTableComponent.setDataList(headers);
        }
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        FuRequestData request = fuHttpRequestData.getRequest();
        if(Objects.nonNull(request)){
            request.setHeaders(fuTableComponent.getDataList());
        }
    }
}
