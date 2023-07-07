package com.wdf.fudoc.request.tab.request;

import com.google.common.collect.Lists;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.apidoc.constant.enumtype.ContentType;
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
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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

    public HttpRequestBodyTab(Disposable disposable) {
        this.noneComponent = new JPanel();
        this.formDataComponent = ChooseFileTableUtils.createTableComponents();
        this.formDataPanel = this.formDataComponent.createPanel();
        this.urlencodedComponent = FuTableComponent.createKeyValue();
        this.urlencodedPanel = this.urlencodedComponent.createPanel();
        this.rawComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE,disposable);
        this.jsonComponent = FuEditorComponent.create(JsonFileType.INSTANCE,disposable);
        this.binaryComponent = new JPanel();
        this.formDataEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE,disposable);
        this.urlencodedEditorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE,disposable);
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

    public void clear(){
        this.jsonComponent.setContent(StringUtils.EMPTY);
        this.urlencodedComponent.setDataList(Lists.newArrayList());
        this.formDataComponent.setDataList(Lists.newArrayList());
        this.rawComponent.setContent(StringUtils.EMPTY);

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
        String json = body.getJson();
        if (StringUtils.isNotBlank(json)) {
            this.jsonComponent.setContent(json);
            this.fuTabComponent.switchTab(JSON);
            return;
        }
        List<KeyValueTableBO> formUrlEncodedList = body.getFormUrlEncodedList();
        if (CollectionUtils.isNotEmpty(formUrlEncodedList)) {
            this.urlencodedComponent.setDataList(formUrlEncodedList);
            this.fuTabComponent.switchTab(FORM_URLENCODED);
            return;
        }

        List<KeyValueTableBO> formDataList = body.getFormDataList();
        if (CollectionUtils.isNotEmpty(formDataList)) {
            this.formDataComponent.setDataList(formDataList);
            this.fuTabComponent.switchTab(FORM_DATA);
            return;
        }
        String raw = body.getRaw();
        if (StringUtils.isNotBlank(raw)) {
            this.rawComponent.setContent(raw);
            this.fuTabComponent.switchTab(RAW);
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
        body.setFormDataList(this.formDataComponent.getDataList());
        body.setFormUrlEncodedList(this.urlencodedComponent.getDataList());
        body.setRaw(this.rawComponent.getContent());
        body.setJson(this.jsonComponent.getContent());
        String currentTab = fuTabComponent.getCurrentTab();
        if (StringUtils.isBlank(currentTab)) {
            return;
        }
        request.addContentType(getContentType(currentTab));
    }


    public static ContentType getContentType(String currentTab) {
        if (FORM_URLENCODED.equals(currentTab)) {
            return ContentType.URLENCODED;
        } else if (JSON.equals(currentTab)) {
            return ContentType.JSON;
        } else if (RAW.equals(currentTab)) {
            return ContentType.RAW;
        } else if (FORM_DATA.equals(currentTab)) {
            return ContentType.FORM_DATA;
        }
        return null;
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
