package com.wdf.fudoc.request.tab.request;

import com.google.common.collect.Lists;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.HeaderKeyValueBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.listener.FuTableDisableListener;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import com.wdf.fudoc.request.view.FuRequestStatusInfoView;
import icons.FuDocIcons;
import org.apache.commons.collections.MapUtils;
import com.wdf.fudoc.util.FuStringUtils;

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
    private final FuTableComponent<HeaderKeyValueBO> fuTableComponent;


    /**
     * 状态信息面板
     */
    private final FuRequestStatusInfoView fuRequestStatusInfoView;

    public ResponseHeaderTabView(Project project) {
        this.fuRequestStatusInfoView = new FuRequestStatusInfoView(project);
        this.fuTableComponent = FuTableComponent.create(FuTableColumnFactory.responseHeader(), HeaderKeyValueBO.class);
        this.fuTableComponent.addListener(new FuTableDisableListener<>());
    }

    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Header", FuDocIcons.FU_REQUEST_HEADER, fuTableComponent.createMainPanel()).builder(this.fuRequestStatusInfoView.getRootPanel());
    }


    /**
     * 初始化请求头数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        FuResponseData response = httpRequestData.getResponse();
        Map<String, List<String>> headers;
        if (Objects.nonNull(response) && MapUtils.isNotEmpty(headers = response.getHeaders())) {
            List<HeaderKeyValueBO> keyValueTableBOList = Lists.newArrayList();
            headers.forEach((key, value) -> {
                HeaderKeyValueBO headerKeyValueBO = new HeaderKeyValueBO();
                headerKeyValueBO.setKey(key);
                headerKeyValueBO.setValue(FuStringUtils.join(value, ";"));
                keyValueTableBOList.add(headerKeyValueBO);
            });
            fuTableComponent.setDataList(keyValueTableBOList);
        }
        //设置响应信息
        fuRequestStatusInfoView.initData(httpRequestData);
    }

}
