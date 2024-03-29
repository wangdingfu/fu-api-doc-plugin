// Copyright 2000-2018 JetBrains s.r.o. Use of this source code is governed by the Apache 2.0 license that can be found in the LICENSE file.
package com.wdf.fudoc.components.action;

import com.intellij.ide.util.ElementsChooser;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.CommonDataKeys;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.openapi.actionSystem.Toggleable;
import com.intellij.openapi.project.DumbAware;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.JBPopup;
import com.intellij.openapi.ui.popup.JBPopupFactory;
import com.intellij.openapi.ui.popup.JBPopupListener;
import com.intellij.openapi.ui.popup.LightweightWindowEvent;
import com.intellij.openapi.util.IconLoader;
import com.intellij.ui.BadgeIconSupplier;
import org.jetbrains.annotations.NonNls;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.HierarchyEvent;
import java.awt.event.HierarchyListener;

public abstract class ShowFilterAction extends ToggleAction implements DumbAware {
    protected JBPopup myFilterPopup;
    private final Icon icon;

    public ShowFilterAction(String text, String desc, Icon icon) {
        super(text, desc, icon);
        this.icon = icon;
    }

    @Override
    public boolean isSelected(@NotNull final AnActionEvent e) {
        return myFilterPopup != null && !myFilterPopup.isDisposed();
    }

    @Override
    public void setSelected(@NotNull final AnActionEvent e, final boolean state) {
        if (state) {
            showPopup(e.getRequiredData(CommonDataKeys.PROJECT), e.getInputEvent().getComponent());
        } else {
            if (myFilterPopup != null && !myFilterPopup.isDisposed()) {
                myFilterPopup.cancel();
            }
        }
    }

    @Override
    public void update(@NotNull AnActionEvent e) {
        e.getPresentation().setIcon(this.icon);
        e.getPresentation().setEnabled(isEnabled());
        Toggleable.setSelected(e.getPresentation(), isSelected(e));
    }

    protected abstract boolean isEnabled();

    protected abstract boolean isActive();

    private void showPopup(@NotNull Project project, @NotNull Component anchor) {
        if (myFilterPopup != null || !anchor.isValid()) {
            return;
        }
        JBPopupListener popupCloseListener = new JBPopupListener() {
            @Override
            public void onClosed(@NotNull LightweightWindowEvent event) {
                myFilterPopup = null;
            }
        };
        myFilterPopup = JBPopupFactory.getInstance()
                .createComponentPopupBuilder(createFilterPanel(), null)
                .setModalContext(false)
                .setFocusable(false)
                .setResizable(true)
                .setCancelOnClickOutside(false)
                .setMinSize(new Dimension(200, 200))
                .setDimensionServiceKey(project, getDimensionServiceKey(), false)
                .addListener(popupCloseListener)
                .createPopup();
        anchor.addHierarchyListener(new HierarchyListener() {
            @Override
            public void hierarchyChanged(HierarchyEvent e) {
                if (e.getID() == HierarchyEvent.HIERARCHY_CHANGED && !anchor.isValid()) {
                    anchor.removeHierarchyListener(this);
                    if (myFilterPopup != null) {
                        myFilterPopup.cancel();
                    }
                }
            }
        });
        myFilterPopup.showUnderneathOf(anchor);
    }

    @NotNull
    @NonNls
    public String getDimensionServiceKey() {
        return "ShowFilterAction_Filter_Popup";
    }

    private JComponent createFilterPanel() {
        ElementsChooser<?> chooser = createChooser();

        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.Y_AXIS));
        panel.add(chooser);
        JPanel buttons = new JPanel();
        JButton all = new JButton("全选");
        all.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                chooser.setAllElementsMarked(true);
            }
        });
        buttons.add(all);
        JButton invert = new JButton("反选");
        invert.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                chooser.invertSelection();
            }
        });
        buttons.add(invert);
        JButton exit = new JButton("退出");
        exit.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(final ActionEvent e) {
                myFilterPopup.cancel();
            }
        });
        buttons.add(exit);
        panel.add(buttons);
        return panel;
    }

    protected abstract ElementsChooser<?> createChooser();
}
