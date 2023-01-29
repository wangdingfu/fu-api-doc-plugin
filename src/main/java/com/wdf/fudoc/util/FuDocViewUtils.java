package com.wdf.fudoc.util;

import com.intellij.find.editorHeaderActions.Utils;
import com.intellij.icons.AllIcons;
import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.intellij.ui.JBColor;
import com.intellij.ui.RelativeFont;
import com.intellij.ui.WindowMoveListener;
import com.intellij.ui.components.JBLabel;
import com.intellij.util.ui.JBUI;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.components.message.MessageComponent;
import com.wdf.fudoc.request.constants.RequestConstants;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author wangdingfu
 * @date 2023-01-29 18:27:16
 */
public class FuDocViewUtils {

    private static final Map<String, AtomicBoolean> pinStatusMap = new ConcurrentHashMap<>();

    public static AtomicBoolean getPinStatus(String title) {
        AtomicBoolean pinStatus = pinStatusMap.get(title);
        if (Objects.isNull(pinStatus)) {
            pinStatus = new AtomicBoolean(false);
            pinStatusMap.put(title, pinStatus);
        }
        return pinStatus;
    }

    public static JPanel createPanel(String title, JComponent content) {
        return createPanel(title, null, content);
    }

    public static JPanel createPanel(String title, DefaultActionGroup actionGroup, JComponent contentCmp) {
        JPanel rootPanel = new JPanel(new BorderLayout());
        JPanel headPanel = headPanel(title, actionGroup);
        rootPanel.add(headPanel, BorderLayout.NORTH);
        rootPanel.add(contentCmp, BorderLayout.CENTER);
        MessageComponent messageComponent = new MessageComponent(true);
        messageComponent.switchInfo();
        rootPanel.add(messageComponent.getRootPanel(), BorderLayout.SOUTH);
        addMouseListeners(rootPanel, headPanel);
        return rootPanel;
    }


    private static JPanel headPanel(String title, DefaultActionGroup actionGroup) {
        JPanel headPanel = new JPanel(new BorderLayout());
        Utils.setSmallerFontForChildren(headPanel);
        headPanel.setBackground(new JBColor(new Color(55, 71, 82), new Color(55, 71, 82)));
        JLabel titleLabel = new JBLabel(title, UIUtil.ComponentStyle.REGULAR);
        titleLabel.setBorder(JBUI.Borders.emptyLeft(5));
        RelativeFont.BOLD.install(titleLabel);
        headPanel.add(titleLabel, BorderLayout.WEST);
        if (Objects.isNull(actionGroup)) {
            actionGroup = new DefaultActionGroup();
        }
        AtomicBoolean status = getPinStatus(title);
        //添加pin
        actionGroup.add(new ToggleAction("Pin", "Pin", AllIcons.General.Pin_tab) {
            @Override
            public boolean isSelected(@NotNull AnActionEvent e) {
                return status.get();
            }

            @Override
            public void setSelected(@NotNull AnActionEvent e, boolean state) {
                status.set(state);
            }
        });
        ToolBarUtils.addActionToToolBar(headPanel, RequestConstants.PLACE_PANEL_TOOLBAR, actionGroup, BorderLayout.EAST);
        return headPanel;
    }

    public static void addMouseListeners(JComponent rootComponent, JComponent focusComponent) {
        WindowMoveListener windowMoveListener = new WindowMoveListener(rootComponent);
        rootComponent.addMouseListener(windowMoveListener);
        rootComponent.addMouseMotionListener(windowMoveListener);
        focusComponent.addMouseListener(windowMoveListener);
        focusComponent.addMouseMotionListener(windowMoveListener);
    }
}
