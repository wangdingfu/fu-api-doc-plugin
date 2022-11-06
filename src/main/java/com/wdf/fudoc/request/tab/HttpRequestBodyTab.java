package com.wdf.fudoc.request.tab;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.request.HttpCallback;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.List;
import java.util.Objects;

/**
 * http请求body tab
 *
 * @author wangdingfu
 * @date 2022-09-17 21:32:03
 */
public class HttpRequestBodyTab implements FuTab, HttpCallback {

    public static final String BODY = "Body";

    private final JPanel noneComponent;
    private final FuTableComponent<KeyValueTableBO> formDataComponent;
    private final JPanel formDataPanel;
    private final FuTableComponent<KeyValueTableBO> urlencodedComponent;
    private final JPanel urlencodedPanel;
    private final FuEditorComponent rawComponent;
    private final FuEditorComponent jsonComponent;
    private final JPanel binaryComponent;
    private final FuEditorComponent formDataEditorComponent;
    private final FuEditorComponent urlencodedEditorComponent;
    private FuTabComponent fuTabComponent;

    public HttpRequestBodyTab() {
        this.noneComponent = new JPanel();
        this.formDataComponent = FuTableComponent.createKeyValueFile();
        this.formDataPanel = this.formDataComponent.createPanel();
        this.urlencodedComponent = FuTableComponent.createKeyValue();
        this.urlencodedPanel = this.urlencodedComponent.createPanel();
        this.rawComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE);
        this.jsonComponent = FuEditorComponent.create(JsonFileType.INSTANCE);
        this.binaryComponent = new JPanel();
        this.formDataEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE);
        this.urlencodedEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE);
    }

    @Override
    public TabInfo getTabInfo() {
        this.fuTabComponent = FuTabComponent.getInstance(BODY, null, this.noneComponent);
        return this.fuTabComponent.addAction("form-data", FuDocIcons.FU_REQUEST_FORM, this.formDataPanel, this.formDataEditorComponent.getMainPanel())
                .addAction("x-www-form-urlencoded", FuDocIcons.FU_REQUEST_URLENCODED, this.urlencodedPanel, this.urlencodedEditorComponent.getMainPanel())
                .addAction("raw", FuDocIcons.FU_REQUEST_RAW, this.rawComponent.getMainPanel())
                .addAction("json", FuDocIcons.FU_REQUEST_JSON, this.jsonComponent.getMainPanel())
                .addAction("binary", FuDocIcons.FU_REQUEST_FILE_BINARY, this.binaryComponent)
                .switchTab("json").builder();
    }


    /**
     * 初始化body部分数据
     *
     * @param httpRequestData 发起http请求的数据
     */
    @Override
    public void initData(FuHttpRequestData httpRequestData) {
        FuRequestData request = httpRequestData.getRequest();
        FuRequestBodyData body = request.getBody();
        List<KeyValueTableBO> formDataList = body.getFormDataList();
        if (CollectionUtils.isNotEmpty(formDataList)) {
            this.formDataComponent.setDataList(formDataList);
            this.fuTabComponent.switchTab("form-data");
        }
        List<KeyValueTableBO> formUrlEncodedList = body.getFormUrlEncodedList();
        if (CollectionUtils.isNotEmpty(formUrlEncodedList)) {
            this.urlencodedComponent.setDataList(formUrlEncodedList);
            this.fuTabComponent.switchTab("x-www-form-urlencoded");
        }
        String raw = body.getRaw();
        if (StringUtils.isNotBlank(raw)) {
            this.rawComponent.setContent(raw);
            this.fuTabComponent.switchTab("raw");
        }
        String json = body.getJson();
        if (StringUtils.isNotBlank(json)) {
            this.jsonComponent.setContent(json);
            this.fuTabComponent.switchTab("json");
        }
    }

    @Override
    public void doSendBefore(FuHttpRequestData fuHttpRequestData) {
        //do nothing
        FuRequestData request = fuHttpRequestData.getRequest();
        FuRequestBodyData body = request.getBody();
        if (Objects.isNull(body)) {
            body = new FuRequestBodyData();
        }
        List<KeyValueTableBO> dataList = this.formDataComponent.getDataList();
        if (CollectionUtils.isNotEmpty(dataList)) {
            body.setFormDataList(dataList);
        }
        List<KeyValueTableBO> urlencodedComponentDataList = this.urlencodedComponent.getDataList();
        if (CollectionUtils.isNotEmpty(urlencodedComponentDataList)) {
            body.setFormUrlEncodedList(urlencodedComponentDataList);
        }
        String content = this.rawComponent.getContent();
        if (StringUtils.isNotBlank(content)) {
            body.setRaw(content);
        }
        String json = this.jsonComponent.getContent();
        if (StringUtils.isNotBlank(json)) {
            body.setJson(json);
        }
    }
}
