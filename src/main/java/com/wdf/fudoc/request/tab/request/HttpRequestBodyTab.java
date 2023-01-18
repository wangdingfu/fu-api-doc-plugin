package com.wdf.fudoc.request.tab.request;

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
import com.wdf.fudoc.request.tab.AbstractBulkEditTabLinkage;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.util.ChooseFileTableUtils;
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
public class HttpRequestBodyTab extends AbstractBulkEditTabLinkage<KeyValueTableBO> implements FuTab, HttpCallback {

    public static final String BODY = "Body";
    public static final String FORM_DATA = "form-data";
    public static final String FORM_URLENCODED = "x-www-form-urlencoded";
    public static final String RAW = "raw";
    public static final String JSON = "json";
    public static final String BINARY = "binary";

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
        this.formDataComponent = ChooseFileTableUtils.createTableComponents();
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
        return this.fuTabComponent.addAction(FORM_DATA, FuDocIcons.FU_REQUEST_FORM, this.formDataPanel, this.formDataEditorComponent.getMainPanel(), this)
                .addAction(FORM_URLENCODED, FuDocIcons.FU_REQUEST_URLENCODED, this.urlencodedPanel, this.urlencodedEditorComponent.getMainPanel(), this)
                .addAction(RAW, FuDocIcons.FU_REQUEST_RAW, this.rawComponent.getMainPanel())
                .addAction(JSON, FuDocIcons.FU_REQUEST_JSON, this.jsonComponent.getMainPanel())
                .addAction(BINARY, FuDocIcons.FU_REQUEST_FILE_BINARY, this.binaryComponent)
                .switchTab(JSON).builder();
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
            this.fuTabComponent.switchTab(FORM_DATA);
        }
        List<KeyValueTableBO> formUrlEncodedList = body.getFormUrlEncodedList();
        if (CollectionUtils.isNotEmpty(formUrlEncodedList)) {
            this.urlencodedComponent.setDataList(formUrlEncodedList);
            this.fuTabComponent.switchTab(FORM_URLENCODED);
        }
        String raw = body.getRaw();
        if (StringUtils.isNotBlank(raw)) {
            this.rawComponent.setContent(raw);
            this.fuTabComponent.switchTab(RAW);
        }
        String json = body.getJson();
        if (StringUtils.isNotBlank(json)) {
            this.jsonComponent.setContent(json);
            this.fuTabComponent.switchTab(JSON);
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

    @Override
    protected FuTableComponent<KeyValueTableBO> getTableComponent(String title) {
        if (FORM_DATA.equals(title)) {
            return this.formDataComponent;
        }
        if (FORM_URLENCODED.equals(title)) {
            return this.urlencodedComponent;
        }
        return null;
    }

    @Override
    protected FuEditorComponent getEditorComponent(String title) {
        if (FORM_DATA.equals(title)) {
            return this.formDataEditorComponent;
        }
        if (FORM_URLENCODED.equals(title)) {
            return this.urlencodedEditorComponent;
        }
        return null;
    }
}
