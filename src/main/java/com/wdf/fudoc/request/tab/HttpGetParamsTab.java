package com.wdf.fudoc.request.tab;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.listener.FuTableListener;
import com.wdf.fudoc.request.InitRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.spring.SpringConfigFileManager;
import com.wdf.fudoc.test.factory.FuTableColumnFactory;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import com.wdf.fudoc.util.PathUtils;
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
public class HttpGetParamsTab implements FuTab, InitRequestData {
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
     * 请求参数
     */
    private List<KeyValueTableBO> params;

    /**
     * http请求数据对象
     */
    private FuHttpRequestData httpRequestData;

    /**
     * 请求地址 不携带请求参数
     */
    private String requestBaseUrl;


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
    }

    /**
     * 将当前容器作为一个tab返回出去
     */
    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Params", null, fuTableComponent.createPanel()).addBulkEditBar(fuEditorComponent.getMainPanel()).builder();
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
            this.params = params;
        }
        //生成请求地址
        genRequestBaseUrl(httpRequestData);
        //重置接口请求地址
        resetRequestUrl();
    }

    /**
     * table组件监听器
     */
    static class HttpGetParamsTableListener implements FuTableListener<KeyValueTableBO> {

        private final HttpGetParamsTab httpGetParamsTab;

        public HttpGetParamsTableListener(HttpGetParamsTab httpGetParamsTab) {
            this.httpGetParamsTab = httpGetParamsTab;
        }

        @Override
        public void propertyChange(KeyValueTableBO data, int row, int column, Object value) {
            //table属性发生了变更 需要重新生成请求地址 且同步数据到批量编辑组件
            httpGetParamsTab.reset(true);
        }
    }


    /**
     * 重置且同步table组件和批量编辑组件内容
     *
     * @param isTable true 从table组件同步到批量编辑组件 false 从批量编辑组件同步到table组件
     */
    public void reset(boolean isTable) {
        if (isTable) {
            //从table组件同步内容到编辑器组件
            this.fuEditorComponent.setContent(buildBulkEditContent(this.params));
        } else {
            // TODO 从编辑器组件同步到table组件
        }
        //重置请求地址
        resetRequestUrl();
    }


    /**
     * 生成不携带请求参数的请求地址
     *
     * @param httpRequestData http请求数据对象
     */
    private void genRequestBaseUrl(FuHttpRequestData httpRequestData) {
        String moduleId = httpRequestData.getModuleId();
        Integer serverPort = SpringConfigFileManager.getServerPort(moduleId);
        FuRequestData request = httpRequestData.getRequest();
        String apiUrl = request.getApiUrl();
        this.requestBaseUrl = PathUtils.urlJoin(FuDocConstants.DEFAULT_HOST + ":" + serverPort, apiUrl);
    }


    /**
     * 重置请求地址
     */
    public void resetRequestUrl() {
        if (Objects.nonNull(this.httpRequestData)) {
            //完整请求地址=域名+apiUrl+请求参数
            String requestUrl = PathUtils.urlJoin(this.requestBaseUrl, joinParams());
            FuRequestData request = this.httpRequestData.getRequest();
            request.setRequestUrl(requestUrl);
            this.requestTabView.setRequestUrl(requestUrl);
        }
    }


    private String joinParams() {
        if (CollectionUtils.isNotEmpty(this.params)) {
            return "?" + this.params.stream().filter(KeyValueTableBO::getSelect).map(this::buildKeyValue).collect(Collectors.joining("&"));
        }
        return StringUtils.EMPTY;
    }

    private String buildBulkEditContent(List<KeyValueTableBO> params) {
        if (CollectionUtils.isNotEmpty(params)) {
            return params.stream().filter(KeyValueTableBO::getSelect).map(this::buildKeyValue).collect(Collectors.joining("\n"));
        }
        return StringUtils.EMPTY;
    }

    private String buildKeyValue(KeyValueTableBO keyValueTableBO) {
        return keyValueTableBO.getKey() + "=" + keyValueTableBO.getValue();
    }

}
