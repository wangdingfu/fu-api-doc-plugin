package com.wdf.fudoc.test.action;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.actionSystem.impl.ActionToolbarImpl;
import com.intellij.openapi.wm.impl.IdeGlassPaneImpl;
import com.intellij.ui.JBColor;
import com.intellij.ui.WindowMoveListener;
import com.wdf.fudoc.test.factory.FuTabBuilder;
import com.wdf.fudoc.util.PopupUtils;
import com.wdf.fudoc.test.view.TestRequestFrom;
import com.wdf.fudoc.components.FuTabComponent;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class TestAction extends AnAction {


    @Override
    public void actionPerformed(@NotNull AnActionEvent e) {
        AtomicBoolean myIsPinned = new AtomicBoolean(false);
        JRootPane rootPane = new JRootPane();
        TestRequestFrom testRequestFrom = new TestRequestFrom(e.getProject());
        JPanel mainPanel = new JPanel(new BorderLayout());

        JPanel headerPanel = new JPanel(new BorderLayout());
        headerPanel.setBackground(new JBColor(new Color(55, 71, 82), new Color(55, 71, 82)));
        JLabel titleLabel = new JLabel("  查看目标详情");
        headerPanel.add(titleLabel, BorderLayout.WEST);
        final ActionManager actionManager = ActionManager.getInstance();
        DefaultActionGroup defaultActionGroup = (DefaultActionGroup) actionManager.getAction("fudoc.request.toolbar.action");
        defaultActionGroup.addSeparator();
        defaultActionGroup.add(new ToggleAction("Pin", "Pin window", AllIcons.General.Pin_tab) {
            @Override
            public boolean isDumbAware() {
                return true;
            }

            @Override
            public boolean isSelected(@NotNull AnActionEvent e) {
                return myIsPinned.get();
            }

            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                myIsPinned.set(state);
            }
        });
        genToolBarPanel(headerPanel, "fudoc.request.test", defaultActionGroup, BorderLayout.EAST);
        FuTabComponent request = FuTabComponent.getInstance("Request", null, testRequestFrom.getRequestPanel());
        mainPanel.add(headerPanel, BorderLayout.NORTH);
        mainPanel.add(FuTabBuilder.getInstance().addTab(request.builder()).addTab(testRequestFrom.createResponseTab()).build(), BorderLayout.CENTER);
        final IdeGlassPaneImpl glass = new IdeGlassPaneImpl(rootPane);
        rootPane.setGlassPane(glass);
        glass.setVisible(true);
        rootPane.setContentPane(mainPanel);
        rootPane.setDefaultButton(testRequestFrom.getSendBtn());
        PopupUtils.create(rootPane, headerPanel, myIsPinned);
    }


    public void genToolBarPanel(JPanel toolBarPanel, String place, ActionGroup actionGroup, String layout) {
        ActionToolbarImpl toolbar = (ActionToolbarImpl) ActionManager.getInstance().createActionToolbar(place, actionGroup, true);
        toolbar.setTargetComponent(toolBarPanel);
        toolbar.setForceMinimumSize(true);
        toolbar.setLayoutPolicy(ActionToolbar.NOWRAP_LAYOUT_POLICY);
        Utils.setSmallerFontForChildren(toolbar);
        toolbar.getComponent().setBackground(toolBarPanel.getBackground());
        toolBarPanel.add(toolbar.getComponent(), layout);
    }




}
