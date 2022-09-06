package com.wdf.fudoc.action;

import com.google.common.collect.Lists;
import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.icons.AllIcons;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.wdf.fudoc.factory.FuTabBuilder;
import com.wdf.fudoc.factory.FuTableColumnFactory;
import com.wdf.fudoc.util.PopupUtils;
import com.wdf.fudoc.view.bo.KeyValueTableBO;
import com.wdf.fudoc.view.components.FuEditorComponent;
import com.wdf.fudoc.view.components.FuTabComponent;
import com.wdf.fudoc.view.components.FuTableComponent;
import icons.FuDocIcons;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;

public class TestAction extends AnAction {
    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {

        JPanel tablePanel1 = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class).createPanel();
        JPanel tablePanel2 = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class).createPanel();
        JPanel editorPanel1 = FuEditorComponent.create(JsonFileType.INSTANCE,"").getMainPanel();
        JPanel editorPanel2 = FuEditorComponent.create(JsonFileType.INSTANCE,"").getMainPanel();
        JPanel editorPanel3 = FuEditorComponent.create(JsonFileType.INSTANCE,"").getMainPanel();
        TabInfo headerTab = FuTabComponent.getInstance("Header", FuDocIcons.FU_REQUEST_HEADER, tablePanel1).addBar("Bulk Edit", AllIcons.Actions.Edit, editorPanel1).builder();
        TabInfo ParamsTab = FuTabComponent.getInstance("Params", FuDocIcons.FU_REQUEST_PARAMS, tablePanel2).addBar("Bulk Edit", AllIcons.Actions.Edit, editorPanel2).builder();
        TabInfo bodyTab = FuTabComponent.getInstance("Body", FuDocIcons.FU_REQUEST_BODY, editorPanel3).builder();
        JPanel popupPanel = FuTabBuilder.getInstance().addTab(headerTab).addTab(ParamsTab).addTab(bodyTab).build();
        PopupUtils.popup(popupPanel);
    }


    private JPanel initToolbar() {
        JPanel toolBarPanel = new BorderLayoutPanel();
        DefaultActionGroup actionGroup = new DefaultActionGroup();

        actionGroup.add(new ToggleAction("批量编辑", "Bulk edit", AllIcons.Actions.Edit) {

            @Override
            public boolean isDumbAware() {
                return true;
            }

            @Override
            public boolean isSelected(@NotNull AnActionEvent e) {
                return false;
            }

            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {

            }
        });


        actionGroup.addSeparator();

        actionGroup.add(new AnAction("Editor", "Editor doc", AllIcons.Actions.Edit) {
            @Override
            public void actionPerformed(@NotNull AnActionEvent e) {
                System.out.println("123");
            }
        });

        ActionToolbarImpl toolbar = (ActionToolbarImpl) ActionManager.getInstance()
                .createActionToolbar("FuRequestToolBar", actionGroup, true);
        toolbar.setTargetComponent(toolBarPanel);
        toolbar.setForceMinimumSize(true);
        toolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
        Utils.setSmallerFontForChildren(toolbar);
        toolBarPanel.add(toolbar.getComponent(), BorderLayout.WEST);
        return toolBarPanel;
    }
}
