package com.wdf.fudoc.test.view;

import cn.fudoc.common.util.ProjectUtils;
import com.google.common.collect.Lists;
import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.Disposable;
import com.intellij.openapi.actionSystem.*;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.JBTabsFactory;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.wdf.fudoc.compat.JsonFileTypeCompat;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTableComponent;
import com.wdf.fudoc.components.bo.KeyValueTableBO;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import icons.FuDocIcons;
import lombok.Getter;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class ToolBarTestForm implements Disposable {
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

        ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar("FuRequestToolBar", actionGroup, true);
        toolbar.setTargetComponent(toolBarPanel);
        // Note: setLayoutPolicy() is deprecated and removed in IDEA 2025.1+
        // The default behavior is already NOWRAP, so this call is not needed
        Utils.setSmallerFontForChildren(toolbar.getComponent());
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
        final JBTabs tabs = JBTabsFactory.createTabs(ProjectUtils.getCurrProject());
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
        this.editPanel = FuEditorComponent.create(JsonFileTypeCompat.getJsonFileType(), "",this).getMainPanel();
        this.bulkEditPanel = createBulkEditBar();
        this.toolBarPanel = new BorderLayoutPanel();
        this.toolBarPanel.add(createTabPanel(),BorderLayout.CENTER);
    }

    @Override
    public void dispose() {

    }
}
