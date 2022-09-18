package com.wdf.fudoc.request.tab;

import com.google.common.collect.Lists;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
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


    public HttpGetParamsTab(RequestTabView requestTabView) {
        this.requestTabView = requestTabView;
        //请求参数表格组件初始化
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class);
        //文本编辑器
        this.fuEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE, "");
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Params", null, fuTableComponent.createPanel())
                .addBulkEditBar(fuEditorComponent.getMainPanel())
                .builder();
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
        //重置接口请求地址
        resetRequestUrl();
    }


    /**
     * 重置请求地址
     */
    private void resetRequestUrl() {
        if (Objects.nonNull(this.httpRequestData)) {
            String moduleId = this.httpRequestData.getModuleId();
            Integer serverPort = SpringConfigFileManager.getServerPort(moduleId);
            FuRequestData request = this.httpRequestData.getRequest();
            String apiUrl = request.getApiUrl();
            //完整请求地址=域名+apiUrl+请求参数
            String requestUrl = PathUtils.urlJoin(FuDocConstants.DEFAULT_HOST + ":" + serverPort, apiUrl, joinParams());
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
