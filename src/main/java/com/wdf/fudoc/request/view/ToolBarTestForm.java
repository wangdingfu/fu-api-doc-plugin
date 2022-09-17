package com.wdf.fudoc.request.view;

import com.google.common.collect.Lists;
import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.icons.AllIcons;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.wdf.fudoc.request.factory.FuTableColumnFactory;
import com.wdf.fudoc.request.view.bo.KeyValueTableBO;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTableComponent;
import icons.FuDocIcons;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ToolBarTestForm {
    @Getter
    private JPanel rootPanel;
    private JPanel toolBarPanel;
    private JPanel contentPanel;


    private JPanel tablePanel;
    private JPanel editPanel;
    private JPanel bulkEditPanel;

    private boolean content = true;

    public ToolBarTestForm() {

        initUI();
        this.contentPanel.setBorder(JBUI.Borders.empty());
        this.toolBarPanel.setBorder(JBUI.Borders.empty());
        this.rootPanel.setBorder(JBUI.Borders.empty());
    }


    private void initUI() {
        changeContent();
    }


    private JPanel createBulkEditBar() {
        JPanel bulkEditBarPanel = new BorderLayoutPanel();
        DefaultActionGroup actionGroup = new DefaultActionGroup();
        actionGroup.add(new AnAction("Bulk Edit", "Bulk edit", AllIcons.Actions.Edit) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                content = !content;
                changeContent();
            }
        });
        ActionToolbarImpl toolbar = (ActionToolbarImpl) ActionManager.getInstance()
                .createActionToolbar("FuRequestToolBar", actionGroup, true);
        toolbar.setTargetComponent(toolBarPanel);
        toolbar.setForceMinimumSize(true);
        toolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
        Utils.setSmallerFontForChildren(toolbar);
        bulkEditBarPanel.add(toolbar.getComponent(), BorderLayout.EAST);
        return bulkEditBarPanel;
    }

    public void changeContent() {
        this.contentPanel.removeAll();
        this.contentPanel.repaint();
        this.contentPanel.add(content ? this.tablePanel : this.editPanel, BorderLayout.CENTER);
        this.contentPanel.revalidate();
    }


    private JComponent createTabPanel() {
        final JBTabsImpl tabs = new JBTabsImpl(null, null, ApplicationManager.getApplication());
        tabs.addTab(createTabInfo("Header", FuDocIcons.FU_REQUEST_HEADER, this.tablePanel).setSideComponent(this.bulkEditPanel));
        tabs.addTab(createTabInfo("Params", FuDocIcons.FU_REQUEST_PARAMS, this.tablePanel).setSideComponent(this.bulkEditPanel));
        tabs.addTab(createTabInfo("Body", FuDocIcons.FU_REQUEST_BODY, this.editPanel));
        return tabs.getComponent();
    }


    private TabInfo createTabInfo(String title, Icon icon, JComponent component) {
        TabInfo tabInfo = new TabInfo(component);
        return tabInfo.setText(title).setIcon(icon);
    }


    private void createUIComponents() {
        this.tablePanel = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class).createPanel();
        this.editPanel = FuEditorComponent.create(JsonFileType.INSTANCE, "").getMainPanel();
        this.bulkEditPanel = createBulkEditBar();
        this.toolBarPanel = new BorderLayoutPanel();
        this.toolBarPanel.add(createTabPanel(),BorderLayout.CENTER);
    }
}
