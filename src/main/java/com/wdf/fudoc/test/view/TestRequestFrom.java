package com.wdf.fudoc.test.view;

import com.google.common.collect.Lists;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.project.Project;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.components.FuTabComponent;
import com.wdf.fudoc.components.factory.FuTabBuilder;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTableComponent;
import icons.FuDocIcons;
import lombok.Getter;

import javax.swing.*;


/**
 * @author wangdingfu
 * @date 2022-09-01 16:22:00
 */
public class TestRequestFrom implements Disposable {
    @Getter
    private JPanel rootPanel;
    private JSplitPane splitPane;

    /**
     * 请求面板
     */
    @Getter
    private JPanel requestPanel;
    //发送面板
    private JPanel sendPanel;
    private JComboBox<String> requestType;
    private JTextField requestUrl;
    @Getter
    private JButton sendBtn;
    //发送tab面板
    private JPanel requestTabPanel;


    /**
     * 响应面板
     */
    private JPanel responsePanel;
    //响应tab面板
    @Getter
    private JPanel responseTabPanel;

    private final Project project;

    public TestRequestFrom(Project project) {
        this.project = project;
        this.requestType.addItem("GET");
        this.requestType.addItem("POST");
        this.requestType.addItem("DELETE");
        this.requestType.addItem("PUT");

        GuiUtils.replaceJSplitPaneWithIDEASplitter(rootPanel, true);
        splitPane.setBorder(JBUI.Borders.empty());
        splitPane.setDividerLocation(0.5);

        this.requestPanel.setBorder(JBUI.Borders.empty());
        this.responsePanel.setBorder(JBUI.Borders.empty());
        this.responseTabPanel.setBorder(JBUI.Borders.empty());
        this.splitPane.setBorder(JBUI.Borders.empty());
        this.sendPanel.setBorder(JBUI.Borders.empty());
    }

    private void createUIComponents() {
        this.requestTabPanel = FuTabBuilder.getInstance().addTab(createHeaderTab()).addTab(createParamsTab()).addTab(createBodyTab()).build();
        this.responseTabPanel = FuTabBuilder.getInstance().addTab(createResponseTab()).build();
    }


    private TabInfo createHeaderTab() {
        return FuTabComponent.getInstance("Header", null, createTablePanel()).addBulkEditBar(createEditorPanel()).builder();
    }

    private TabInfo createParamsTab() {
        return FuTabComponent.getInstance("Params", null, createTablePanel()).addBulkEditBar(createEditorPanel()).builder();
    }

    private TabInfo createBodyTab() {
        return FuTabComponent.getInstance("Body", null, createEditorPanel())
                .addAction("none", FuDocIcons.FU_REQUEST_IGNORE, createTablePanel())
                .addAction("form-data", FuDocIcons.FU_REQUEST_FORM, createTable1Panel(), createEditorPanel(), null)
                .addAction("x-www-form-urlencoded", FuDocIcons.FU_REQUEST_URLENCODED, createTablePanel())
                .addAction("raw", FuDocIcons.FU_REQUEST_RAW, createEditorPanel())
                .addAction("json", FuDocIcons.FU_REQUEST_JSON, createEditorPanel())
                .addAction("binary", FuDocIcons.FU_REQUEST_FILE_BINARY, createTablePanel())
                .switchTab("json").builder();
    }


    public TabInfo createResponseTab() {
        return FuTabComponent.getInstance("Response", null, createEditorPanel()).builder();
    }

    private TabInfo createRowTab() {
        return FuTabComponent.getInstance("Raw", null, createEditorPanel()).builder();
    }

    private JPanel createTablePanel() {
        return FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class).createPanel();
    }


    private JPanel createTable1Panel() {
        FuTableComponent<KeyValueTableBO> component = FuTableComponent.create(FuTableColumnFactory.formDataColumns(), Lists.newArrayList(), KeyValueTableBO.class);
        return component.createPanel();
    }

    private JPanel createEditorPanel() {
        return FuEditorComponent.create(JsonFileType.INSTANCE, "",this).getMainPanel();
    }

    @Override
    public void dispose() {

    }
}
