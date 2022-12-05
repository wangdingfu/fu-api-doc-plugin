package com.wdf.fudoc.test.view;

import com.google.common.collect.Lists;
import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.icons.AllIcons;
import com.intellij.json.JsonFileType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import com.intellij.util.ui.components.BorderLayoutPanel;
import com.wdf.fudoc.components.factory.FuTableColumnFactory;
import com.wdf.fudoc.test.view.bo.KeyValueTableBO;
import com.wdf.fudoc.components.FuEditorComponent;
import com.wdf.fudoc.components.FuTableComponent;
import lombok.Getter;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class TestView1 {
    @Getter
    private JPanel rootPanel;
    private JPanel topPanel;
    private JPanel centerPanel;
    @NonNls
    public static final String FU_REQUEST_POPUP = "fudoc.request.popup";

    private JBPopup popup;
    private JPanel toolBarPanel;
    private final Project project;

    public void popup() {

        // dialog 改成 popup, 第一个为根面板，第二个为焦点面板
        popup = JBPopupFactory.getInstance().createComponentPopupBuilder(rootPanel,toolBarPanel)
                .setProject(project)
                .setResizable(true)
                .setMovable(true)

                .setModalContext(false)
                .setRequestFocus(true)
                .setBelongsToGlobalPopupStack(true)
                .setDimensionServiceKey(null, FU_REQUEST_POPUP, true)
                .setLocateWithinScreenBounds(false)
                // 鼠标点击外部时是否取消弹窗 外部单击, 未处于 pin 状态则可关闭
                .setCancelOnMouseOutCallback(event -> event.getID() == MouseEvent.MOUSE_PRESSED)

                // 单击外部时取消弹窗
                .setCancelOnClickOutside(false)
                // 在其他窗口打开时取消
                .setCancelOnOtherWindowOpen(false)
                .setCancelOnWindowDeactivation(false)
                .createPopup();
        popup.showCenteredInCurrentWindow(project);
    }


    public TestView1(Project project) {
        this.project = project;

    }

    private void createUIComponents() {
        final JBTabsImpl tabs = new JBTabsImpl(null, null, ApplicationManager.getApplication());
        initToolbar();
        tabs.addTab(new TabInfo(FuEditorComponent.create(JsonFileType.INSTANCE, "").getMainPanel()).setText("Body"));
        tabs.addTab(new TabInfo(FuEditorComponent.create(JsonFileType.INSTANCE,"").getMainPanel()).setText("Params"));
        tabs.addTab(new TabInfo(FuEditorComponent.create(JsonFileType.INSTANCE,"").getMainPanel()).setText("Header").setSideComponent(this.toolBarPanel));
        this.topPanel = new BorderLayoutPanel();
        this.topPanel.add(tabs.getComponent(),BorderLayout.CENTER);
        this.centerPanel = FuTableComponent.create(FuTableColumnFactory.keyValueColumns(), Lists.newArrayList(), KeyValueTableBO.class).createPanel();
    }


    private void initToolbar() {
        this.toolBarPanel = new BorderLayoutPanel();
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
        toolBarPanel.add(toolbar.getComponent(), BorderLayout.EAST);
    }
}
