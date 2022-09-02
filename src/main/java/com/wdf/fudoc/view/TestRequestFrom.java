package com.wdf.fudoc.view;

import com.google.common.collect.Lists;
import com.intellij.json.JsonFileType;
import com.wdf.fudoc.factory.FuTableColumnFactory;
import com.wdf.fudoc.view.bo.KeyValueBO;
import com.wdf.fudoc.view.components.FuEditorComponent;
import com.wdf.fudoc.view.components.FuTableComponent;
import lombok.Getter;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2022-09-01 16:22:00
 */
public class TestRequestFrom {
    @Getter
    private JPanel rootPanel;
    private JPanel requestPanel;
    private JPanel responsePanel;
    private JTextField requestUrl;
    private JButton sendBtn;
    private JTabbedPane requestTabPanel;
    private JPanel requestHeaderPanel;
    private JPanel sendPanel;
    private JComboBox<String> requestType;
    private JTabbedPane responseTabPanel;
    private JPanel requestBodyPanel;
    private JPanel responseBodyPanel;
    private JPanel requestParamPanel;
    private JPanel responseRowPanel;

    public TestRequestFrom() {
        this.requestType.addItem("GET");
        this.requestType.addItem("POST");
        this.requestType.addItem("DELETE");
        this.requestType.addItem("PUT");

    }

    private void createUIComponents() {
        this.requestHeaderPanel = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueBO.class).createPanel();
        this.requestBodyPanel = FuEditorComponent.create(JsonFileType.INSTANCE,"").getMainPanel();
        this.responseBodyPanel = FuEditorComponent.create(JsonFileType.INSTANCE,"").getMainPanel();
        this.responseRowPanel = FuEditorComponent.create(JsonFileType.INSTANCE,"").getMainPanel();
        this.requestParamPanel = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueBO.class).createPanel();

    }
}
