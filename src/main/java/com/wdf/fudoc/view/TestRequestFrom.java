package com.wdf.fudoc.view;

import com.google.common.collect.Lists;
import com.intellij.icons.AllIcons;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.project.Project;
import com.intellij.ui.GuiUtils;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.JBUI;
import com.wdf.fudoc.constant.enumtype.RequestParamType;
import com.wdf.fudoc.factory.FuTabBuilder;
import com.wdf.fudoc.factory.FuTableColumnFactory;
import com.wdf.fudoc.factory.TableCellEditorFactory;
import com.wdf.fudoc.view.bo.KeyValueTableBO;
import com.wdf.fudoc.view.components.*;
import icons.FuDocIcons;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import javax.swing.table.TableCellEditor;

/**
 * @author wangdingfu
 * @date 2022-09-01 16:22:00
 */
public class TestRequestFrom {
    @Getter
    private JPanel rootPanel;
    private JSplitPane splitPane;

    /**
     * 请求面板
     */
    private JPanel requestPanel;
    //发送面板
    private JPanel sendPanel;
    private JComboBox<String> requestType;
    private JTextField requestUrl;
    private JButton sendBtn;
    //发送tab面板
    private JPanel requestTabPanel;


    /**
     * 响应面板
     */
    private JPanel responsePanel;
    //响应tab面板
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

        this.sendBtn.setEnabled(true);

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
        return FuTabComponent.getInstance("Header", null, createTablePanel()).addBar("Bulk Edit", FuDocIcons.FU_REQUEST_BULK_EDIT, createEditorPanel()).builder();
    }

    private TabInfo createParamsTab() {
        return FuTabComponent.getInstance("Params", null, createTablePanel()).addBar("Bulk Edit", FuDocIcons.FU_REQUEST_BULK_EDIT, createEditorPanel()).builder();
    }

    private TabInfo createBodyTab() {
        return FuTabComponent.getInstance("Body", null, createEditorPanel())
                .addToggleBar("none", FuDocIcons.FU_REQUEST_IGNORE, createTablePanel())
                .addToggleBar("form-data", FuDocIcons.FU_REQUEST_FORM, createTable1Panel())
                .addToggleBar("x-www-form-urlencoded", FuDocIcons.FU_REQUEST_URLENCODED, createTablePanel())
                .addToggleBar("raw", FuDocIcons.FU_REQUEST_RAW, createEditorPanel())
                .addToggleBar("json", FuDocIcons.FU_REQUEST_JSON, createEditorPanel())
                .addToggleBar("binary", FuDocIcons.FU_REQUEST_FILE_BINARY, createTablePanel())
                .addBar("Bulk Edit", FuDocIcons.FU_REQUEST_BULK_EDIT, createEditorPanel())
                .setDefaultTab("json").builder();
    }


    private TabInfo createResponseTab() {
        return FuTabComponent.getInstance("Response", null, createEditorPanel())
                .addToggleBar("json", FuDocIcons.FU_REQUEST_JSON, createEditorPanel())
                .addToggleBar("raw", FuDocIcons.FU_REQUEST_RAW, createEditorPanel())
                .setDefaultTab("json").builder();
    }

    private TabInfo createRowTab() {
        return FuTabComponent.getInstance("Raw", null, createEditorPanel()).builder();
    }

    private JPanel createTablePanel() {
        return FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class).createPanel();
    }


    private JPanel createTable1Panel() {
        FuTableComponent<KeyValueTableBO> component = FuTableComponent.create(FuTableColumnFactory.keyValueColumns1(), Lists.newArrayList(), KeyValueTableBO.class);
        component.addListener((fuTableView, row, column) -> {
            if (column == 3) {
                Object valueAt = fuTableView.getValueAt(row, column - 1);
                if (RequestParamType.FILE.getCode().equals(valueAt)) {
                    return TableCellEditorFactory.createLocalPathCellEditor();
                }
            }
            return null;
        });
        return component.createPanel();
    }

    private JPanel createEditorPanel() {
        return FuEditorComponent.create(JsonFileType.INSTANCE, "").getMainPanel();
    }
}
