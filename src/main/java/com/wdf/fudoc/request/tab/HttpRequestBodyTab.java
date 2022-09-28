package com.wdf.fudoc.request.tab;

import com.intellij.json.JsonFileType;
import com.intellij.openapi.fileTypes.PlainTextFileType;
import com.intellij.ui.tabs.TabInfo;
import com.wdf.fudoc.apidoc.constant.enumtype.RequestType;
import com.wdf.fudoc.common.FuTab;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.request.InitRequestData;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.fudoc.request.pojo.FuRequestBodyData;
import com.wdf.fudoc.request.pojo.FuRequestData;
import com.wdf.fudoc.test.factory.FuTableColumnFactory;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import com.wdf.fudoc.util.FuComponentsUtils;
import icons.FuDocIcons;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.List;

/**
 * http请求body tab
 *
 * @author wangdingfu
 * @date 2022-09-17 21:32:03
 */
public class HttpRequestBodyTab implements FuTab, InitRequestData {

    public static final String BODY = "Body";

    private final JPanel noneComponent;
    private final FuTableComponent<KeyValueTableBO> formDataComponent;
    private final JPanel formDataPanel;
    private final FuTableComponent<KeyValueTableBO> urlencodedComponent;
    private final JPanel urlencodedPanel;
    private final FuEditorComponent rawComponent;
    private final JPanel rawPanel;
    private final FuEditorComponent jsonComponent;
    private final JPanel jsonPanel;
    private final JPanel binaryComponent;
    private final FuEditorComponent editorComponent;
    private final JPanel bulkEditPanel;

    private FuTabComponent fuTabComponent;


    public HttpRequestBodyTab() {
        this.noneComponent = new JPanel();
        this.formDataComponent = FuTableComponent.createKeyValueFile();
        this.formDataPanel = this.formDataComponent.createPanel();
        this.urlencodedComponent = FuTableComponent.createKeyValue();
        this.urlencodedPanel = this.urlencodedComponent.createPanel();
        this.rawComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE);
        this.rawPanel = this.rawComponent.getMainPanel();
        this.jsonComponent = FuEditorComponent.create(JsonFileType.INSTANCE);
        this.jsonPanel = this.jsonComponent.getMainPanel();
        this.binaryComponent = new JPanel();
        this.editorComponent = FuEditorComponent.create(PlainTextFileType.INSTANCE);
        this.bulkEditPanel = this.editorComponent.getMainPanel();
    }

    @Override
    public TabInfo getTabInfo() {
        this.fuTabComponent = FuTabComponent.getInstance("Body", null, this.noneComponent);
        return fuTabComponent.addToggleBar("form-data", FuDocIcons.FU_REQUEST_FORM, this.formDataPanel)
                .addToggleBar("x-www-form-urlencoded", FuDocIcons.FU_REQUEST_URLENCODED, this.urlencodedPanel)
                .addToggleBar("raw", FuDocIcons.FU_REQUEST_RAW, this.rawPanel)
                .addToggleBar("json", FuDocIcons.FU_REQUEST_JSON, this.jsonPanel)
                .addToggleBar("binary", FuDocIcons.FU_REQUEST_FILE_BINARY, this.binaryComponent)
                .addBar("Bulk Edit", FuDocIcons.FU_REQUEST_BULK_EDIT, this.bulkEditPanel)
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
}
