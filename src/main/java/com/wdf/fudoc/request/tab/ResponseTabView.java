package com.wdf.fudoc.request.tab;

import cn.hutool.json.JSONUtil;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.request.InitRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuResponseData;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * http响应部分内容
 *
 * @author wangdingfu
 * @date 2022-09-17 18:05:45
 */
public class ResponseTabView implements FuTab, InitRequestData {

    public static final String RESPONSE = "Response";

    private final Project project;

    private final FuEditorComponent fuEditorComponent;

    public ResponseTabView(Project project) {
        this.project = project;
        this.fuEditorComponent = FuEditorComponent.create(JsonFileType.INSTANCE, "");
    }


    @Override
    public TabInfo getTabInfo() {
        return FuTabComponent.getInstance("Response", null, fuEditorComponent.getMainPanel()).builder();
    }


    /**
     * 初始化响应数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        FuResponseData response = httpRequestData.getResponse();
        String content;
        if (Objects.nonNull(response) && StringUtils.isNoneBlank(content = response.getContent())) {
            fuEditorComponent.setContent(JSONUtil.formatJsonStr(content));
        }
    }
}
