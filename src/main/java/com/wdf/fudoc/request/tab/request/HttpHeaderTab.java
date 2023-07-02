package com.wdf.fudoc.request.tab.request;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.apidoc.constant.enumtype.HeaderLevel;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.HeaderKeyValueBO;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.po.GlobalKeyValuePO;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.request.tab.AbstractBulkEditTabLinkage;
import com.wdf.fudoc.storage.FuRequestConfigStorage;
import com.wdf.fudoc.storage.factory.FuRequestConfigStorageFactory;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * http请求头tab页
 *
 * @author wangdingfu
 * @date 2022-09-17 21:30:58
 */
public class HttpHeaderTab extends AbstractBulkEditTabLinkage<HeaderKeyValueBO> implements FuTab, HttpCallback {

    private final Project project;
    /**
     * table组件
     */
    private final FuTableComponent<HeaderKeyValueBO> fuTableComponent;
    /**
     * 批量编辑请求参数组件
     */
    private final FuEditorComponent fuEditorComponent;

    private Module module;

    public HttpHeaderTab(Project project) {
        this.project = project;
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.header(), HeaderKeyValueBO.class);
        //文本编辑器
        this.fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "");
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Header", null, fuTableComponent.createPanel()).addBulkEditBar(this.fuEditorComponent.getMainPanel(), this).builder();
    }


    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        //切换tab时需要更新全局请求头
        if (Objects.isNull(module)) {
            return;
        }
        FuRequestConfigStorage fuRequestConfigStorage = FuRequestConfigStorageFactory.get(project);
        List<GlobalKeyValuePO> globalHeaderList = fuRequestConfigStorage.readData().getGlobalHeaderList();
        if (CollectionUtils.isNotEmpty(globalHeaderList)) {
            addHeader(globalHeaderList.stream().filter(f -> Objects.isNull(f.getScope()) || f.getScope().isScope(module)).collect(Collectors.toList()));
        }
    }

    /**
     * 初始化请求头数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        this.module = httpRequestData.getModule();
        addHeader(httpRequestData.getRequest().getHeaders());
    }


    private void addHeader(List<HeaderKeyValueBO> headers) {
        if (CollectionUtils.isEmpty(headers)) {
            return;
        }
        List<HeaderKeyValueBO> dataList = Lists.newArrayList(this.fuTableComponent.getDataList());
        if (CollectionUtils.isNotEmpty(dataList)) {
            //移除重复的
            dataList.removeIf(f -> headers.stream().anyMatch(a -> a.getKey().equals(f.getKey())));
        }
        dataList.addAll(headers);
        this.fuTableComponent.setDataList(dataList);
    }


    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        FuRequestData request = fuHttpRequestData.getRequest();
        if (Objects.nonNull(request)) {
            List<HeaderKeyValueBO> dataList = fuTableComponent.getDataList();
            if (CollectionUtils.isNotEmpty(dataList)) {
                //请求对象中无需保存全局请求头 在实际请求时会统一带上全局请求头
                dataList.removeIf(f -> HeaderLevel.GLOBAL.getView().equals(f.getLevel()));
            }
            request.setHeaders(dataList);
        }
    }

    @Override
    protected FuTableComponent<HeaderKeyValueBO> getTableComponent(String tab) {
        return this.fuTableComponent;
    }

    @Override
    protected FuEditorComponent getEditorComponent(String tab) {
        return this.fuEditorComponent;
    }
}
