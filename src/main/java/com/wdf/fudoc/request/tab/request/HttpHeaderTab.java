package com.wdf.fudoc.request.tab.request;

import cn.hutool.core.bean.BeanUtil;
import com.google.common.collect.Lists;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.data.FuRequestSettingData;
import com.wdf.fudoc.request.pojo.CommonHeader;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.state.FuRequestSettingState;
import com.wdf.fudoc.request.tab.AbstractBulkEditTabLinkage;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;

/**
 * http请求头tab页
 *
 * @author wangdingfu
 * @date 2022-09-17 21:30:58
 */
public class HttpHeaderTab extends AbstractBulkEditTabLinkage<KeyValueTableBO> implements FuTab, HttpCallback {

    /**
     * table组件
     */
    private final FuTableComponent<KeyValueTableBO> fuTableComponent;
    /**
     * 批量编辑请求参数组件
     */
    private final FuEditorComponent fuEditorComponent;

    public HttpHeaderTab() {
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class);
        //文本编辑器
        this.fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "");
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Header", null, fuTableComponent.createPanel())
                .addBulkEditBar(this.fuEditorComponent.getMainPanel(), this)
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
        FuRequestSettingData data = FuRequestSettingState.getData();
        List<CommonHeader> commonHeaderList = data.getCommonHeaderList();
        List<KeyValueTableBO> headerList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(commonHeaderList)) {
            //填充公共请求头
            headerList.addAll(BeanUtil.copyToList(commonHeaderList, KeyValueTableBO.class));
        }
        if (CollectionUtils.isNotEmpty(headers)) {
            headerList.addAll(headers);
        }
        this.fuTableComponent.setDataList(headerList);
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        FuRequestData request = fuHttpRequestData.getRequest();
        if (Objects.nonNull(request)) {
            request.setHeaders(fuTableComponent.getDataList());
        }
    }

    @Override
    protected FuTableComponent<KeyValueTableBO> getTableComponent(String tab) {
        return this.fuTableComponent;
    }

    @Override
    protected FuEditorComponent getEditorComponent(String tab) {
        return this.fuEditorComponent;
    }
}
