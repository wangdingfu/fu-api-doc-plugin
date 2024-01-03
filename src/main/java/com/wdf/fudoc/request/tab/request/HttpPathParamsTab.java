package com.wdf.fudoc.request.tab.request;

import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-06 14:19:59
 */
public class HttpPathParamsTab implements FuTab, HttpCallback {
    public static final String TITLE = "Path";


    private final FuTableComponent<KeyValueTableBO> pathTable;
    private final RequestTabView requestTabView;
    private FuHttpRequestData httpRequestData;

    public HttpPathParamsTab(RequestTabView requestTabView) {
        this.requestTabView = requestTabView;
        this.pathTable = FuTableComponent.create(FuTableColumnFactory.pathVariable(), KeyValueTableBO.class);
        this.pathTable.addListener(new HttpPathParamsTableListener(this));

    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance(TITLE, null, this.pathTable.createMainPanel()).builder();
    }

    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        FuRequestData request;
        if (Objects.isNull(httpRequestData) || Objects.isNull(request = httpRequestData.getRequest())) {
            return;
        }
        this.httpRequestData = httpRequestData;
        List<KeyValueTableBO> pathVariables = request.getPathVariables();
        if (CollectionUtils.isNotEmpty(pathVariables)) {
            this.pathTable.setDataList(pathVariables);
            resetRequestUrlFromTable();
        }
    }

    @Override
    public void resetParams(Map<String, String> param) {
        if (MapUtils.isEmpty(param)) {
            return;
        }
        List<KeyValueTableBO> dataList = pathTable.getDataList();
        param.forEach((key, value) -> {
            if (dataList.stream().noneMatch(a -> a.getKey().equals(key))) {
                pathTable.addRowData(new KeyValueTableBO(true, key, value));
            }
        });

    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        FuRequestData request;
        if (Objects.isNull(fuHttpRequestData) || Objects.isNull(request = fuHttpRequestData.getRequest())) {
            return;
        }
        request.setPathVariables(this.pathTable.getDataList());
    }


    /**
     * table组件监听器 当table组件中的数据发生变化时 需要更新url
     */
    static class HttpPathParamsTableListener implements FuTableListener<KeyValueTableBO> {

        private final HttpPathParamsTab httpPathParamsTab;

        public HttpPathParamsTableListener(HttpPathParamsTab httpPathParamsTab) {
            this.httpPathParamsTab = httpPathParamsTab;
        }

        @Override
        public void addRow(KeyValueTableBO data) {
            this.httpPathParamsTab.resetRequestUrlFromTable();
        }

        @Override
        public void propertyChange(KeyValueTableBO data, int row, int column, Object value) {
            //table属性发生了变更 需要重新生成请求地址
            this.httpPathParamsTab.resetRequestUrlFromTable();
        }

        @Override
        public void deleteRow(int row) {
            this.httpPathParamsTab.resetRequestUrlFromTable();
        }

        @Override
        public void exchangeRows(int oldIndex, int newIndex) {
            this.httpPathParamsTab.resetRequestUrlFromTable();
        }
    }

    /**
     * 重置请求地址
     */
    public void resetRequestUrlFromTable() {
        if (Objects.nonNull(this.httpRequestData)) {
            FuRequestData request = this.httpRequestData.getRequest();
            request.setRequestUrl(null);
            this.requestTabView.setRequestUrl(request.getRequestUrl());
        }
    }


}
