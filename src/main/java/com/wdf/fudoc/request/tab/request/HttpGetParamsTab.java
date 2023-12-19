package com.wdf.fudoc.request.tab.request;

import com.google.common.collect.Lists;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuEditorListener;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.request.tab.AbstractBulkEditTabLinkage;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * http请求参数tab页【GET请求参数】
 *
 * @author wangdingfu
 * @date 2022-09-17 21:31:31
 */
public class HttpGetParamsTab extends AbstractBulkEditTabLinkage<KeyValueTableBO> implements FuTab, HttpCallback {

    public static final String TITLE = "Query";
    /**
     * 请求参数table组件
     */
    private final FuTableComponent<KeyValueTableBO> fuTableComponent;

    /**
     * 批量编辑请求参数组件
     */
    private final FuEditorComponent fuEditorComponent;

    /**
     * 请求tab页
     */
    private final RequestTabView requestTabView;

    /**
     * http请求数据对象
     */
    private FuHttpRequestData httpRequestData;


    private FuTabComponent fuTabComponent;


    /**
     * 初始化GET请求的table组件和批量编辑组件
     *
     * @param requestTabView 父容器对象
     */
    public HttpGetParamsTab(RequestTabView requestTabView, Disposable disposable) {
        this.requestTabView = requestTabView;
        //请求参数表格组件初始化
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class);
        this.fuTableComponent.addListener(new HttpGetParamsTableListener(this));
        //文本编辑器
        this.fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "",disposable);
        this.fuEditorComponent.addListener(new HttpGetEditorListener(this));
    }

    /**
     * 将当前容器作为一个tab返回出去
     */
    @Override
    public TabInfo getTabInfo() {
        this.fuTabComponent = FuTabComponent.getInstance(TITLE, null, this.fuTableComponent.createPanel());
        return fuTabComponent.addBulkEditBar(fuEditorComponent.getMainPanel(), this).builder();
    }


    /**
     * 初始化请求头数据
     *
     * @param httpRequestData http请求数据对象
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        this.httpRequestData = httpRequestData;
        FuRequestData request = httpRequestData.getRequest();
        List<KeyValueTableBO> params = request.getParams();
        if (CollectionUtils.isNotEmpty(params)) {
            this.fuTableComponent.setDataList(params);
            this.fuEditorComponent.setContent(buildBulkEditContent(params));
        }
        //重置接口请求地址
        resetRequestUrlFromTable();
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        //将当前激活面板的数据同步到另一个面板 保证两个面板数据一致
        onClick(null, fuTabComponent.getTabActionBO(TITLE));
        //设置最新数据到请求对象中
        FuRequestData request = fuHttpRequestData.getRequest();
        request.setParams(this.fuTableComponent.getDataList());
    }

    @Override
    public void selectionChanged(TabInfo oldSelection, TabInfo newSelection) {
        //切换到当前tab
        if(Objects.nonNull(this.httpRequestData)){
            FuRequestData request = httpRequestData.getRequest();
            request.removeHeader(FuDocConstants.CONTENT_TYPE);
        }
    }

    /**
     * 重置table中的请求参数
     *
     * @param param 请求参数
     */
    @Override
    public void resetParams(Map<String, String> param) {
        List<KeyValueTableBO> dataList = this.fuTableComponent.getDataList();
        List<KeyValueTableBO> tableList = Lists.newArrayList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            for (KeyValueTableBO keyValueTableBO : dataList) {
                String value = param.remove(keyValueTableBO.getKey());
                if(StringUtils.isNotBlank(value)){
                    keyValueTableBO.setValue(value);
                    keyValueTableBO.setSelect(true);
                }else {
                    keyValueTableBO.setSelect(false);
                }
                tableList.add(keyValueTableBO);
            }
        }
        //将url中的请求参数添加到table中
        param.forEach((key, value) -> tableList.add(new KeyValueTableBO(true, key, value)));
        this.fuTableComponent.setDataList(tableList);
    }

    @Override
    protected FuTableComponent<KeyValueTableBO> getTableComponent(String tab) {
        return this.fuTableComponent;
    }

    @Override
    protected FuEditorComponent getEditorComponent(String tab) {
        return this.fuEditorComponent;
    }



    /**
     * table组件监听器 当table组件中的数据发生变化时 需要更新url
     */
    static class HttpGetParamsTableListener implements FuTableListener<KeyValueTableBO> {

        private final HttpGetParamsTab httpGetParamsTab;

        public HttpGetParamsTableListener(HttpGetParamsTab httpGetParamsTab) {
            this.httpGetParamsTab = httpGetParamsTab;
        }

        @Override
        public void addRow(KeyValueTableBO data) {
            this.httpGetParamsTab.resetRequestUrlFromTable();
        }

        @Override
        public void propertyChange(KeyValueTableBO data, int row, int column, Object value) {
            //table属性发生了变更 需要重新生成请求地址
            this.httpGetParamsTab.resetRequestUrlFromTable();
        }

        @Override
        public void deleteRow(int row) {
            this.httpGetParamsTab.resetRequestUrlFromTable();
        }

        @Override
        public void exchangeRows(int oldIndex, int newIndex) {
            this.httpGetParamsTab.resetRequestUrlFromTable();
        }
    }


    /**
     * 编辑器监听器 当编辑器中的文本内容发生变化时 需要更新请求url
     */
    static class HttpGetEditorListener implements FuEditorListener {
        private final HttpGetParamsTab httpGetParamsTab;

        public HttpGetEditorListener(HttpGetParamsTab httpGetParamsTab) {
            this.httpGetParamsTab = httpGetParamsTab;
        }

        @Override
        public void contentChange(String content) {
            //重置请求地址
            httpGetParamsTab.resetRequestUrlFromEditor();
        }
    }


    /**
     * 依据table组件的数据来重置请求地址
     */
    public void resetRequestUrlFromTable() {
        resetRequestUrl(joinParamsFromTable());
    }

    /**
     * 依据编辑器组件的数据来重置请求地址
     */
    public void resetRequestUrlFromEditor() {
        resetRequestUrl(joinParamsFromEditor());
    }


    /**
     * 根据表格组件的请求参数拼接成请求url追加的参数
     */
    public String joinParamsFromTable() {
        return joinParams(this.fuTableComponent.getDataList());
    }

    /**
     * 根据编辑器组件的请求参数拼接成请求url追加的参数
     */
    private String joinParamsFromEditor() {
        return joinParams(editorToTableData());
    }


    /**
     * 拼接请求参数为GET请求参数格式
     *
     * @param dataList 请求参数集合
     * @return url后追加的参数
     */
    private String joinParams(List<KeyValueTableBO> dataList) {
        if (CollectionUtils.isNotEmpty(dataList)) {
            return dataList.stream().filter(KeyValueTableBO::getSelect).map(this::buildKeyValue).collect(Collectors.joining("&"));
        }
        return StringUtils.EMPTY;
    }

    /**
     * 重置请求地址
     */
    public void resetRequestUrl(String paramUrl) {
        if (Objects.nonNull(this.httpRequestData)) {
            FuRequestData request = this.httpRequestData.getRequest();
            request.setParamUrl(paramUrl);
            request.setRequestUrl(null);
            this.requestTabView.setRequestUrl(request.getRequestUrl());
        }
    }


    private String buildKeyValue(KeyValueTableBO keyValueTableBO) {
        return keyValueTableBO.getKey() + "=" + keyValueTableBO.getValue();
    }

}
