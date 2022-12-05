package com.wdf.fudoc.request.tab;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.listener.FuEditorListener;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * http请求参数tab页【GET请求参数】
 *
 * @author wangdingfu
 * @date 2022-09-17 21:31:31
 */
public class HttpGetParamsTab extends AbstractBulkEditTabLinkage implements FuTab, HttpCallback {

    public static final String PARAMS = "Params";
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


    /**
     * 初始化GET请求的table组件和批量编辑组件
     *
     * @param requestTabView 父容器对象
     */
    public HttpGetParamsTab(RequestTabView requestTabView) {
        this.requestTabView = requestTabView;
        //请求参数表格组件初始化
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class);
        this.fuTableComponent.addListener(new HttpGetParamsTableListener(this));
        //文本编辑器
        this.fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "");
        this.fuEditorComponent.addListener(new HttpGetEditorListener(this));
    }

    /**
     * 将当前容器作为一个tab返回出去
     */
    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(PARAMS, null, fuTableComponent.createPanel())
                .addBulkEditBar(fuEditorComponent.getMainPanel(), this).builder();
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
        //初始化请求地址
        initRequestUrl(httpRequestData);
        //重置接口请求地址
        resetRequestUrlFromTable();
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        //do nothing 当前tab页的请求地址已经实时更新到request对象中
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
     * 生成不携带请求参数的请求地址
     *
     * @param httpRequestData http请求数据对象
     */
    private void initRequestUrl(FuHttpRequestData httpRequestData) {
        FuRequestData request = httpRequestData.getRequest();
        String requestUrl = request.getRequestUrl();
        if (StringUtils.isBlank(requestUrl)) {
            String baseUrl = request.getBaseUrl();
            //接口地址中参数替换
            List<KeyValueTableBO> pathVariables = request.getPathVariables();
            if (CollectionUtils.isNotEmpty(pathVariables)) {
                for (KeyValueTableBO pathVariable : pathVariables) {
                    String key = pathVariable.getKey();
                    baseUrl = StringUtils.replace(baseUrl, "{" + key + "}", pathVariable.getValue());
                }
            }
            request.setBaseUrl(baseUrl);
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
            this.requestTabView.setRequestUrl(request.getRequestUrl());
        }
    }


    private String buildKeyValue(KeyValueTableBO keyValueTableBO) {
        return keyValueTableBO.getKey() + "=" + keyValueTableBO.getValue();
    }

}
