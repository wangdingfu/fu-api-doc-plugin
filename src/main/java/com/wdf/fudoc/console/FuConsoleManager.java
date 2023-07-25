package com.wdf.fudoc.console;

import com.intellij.execution.executors.DefaultRunExecutor;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.RunContentDescriptor;
import com.intellij.execution.ui.RunContentManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.ui.UIUtil;
import com.wdf.fudoc.common.constant.FuDocConstants;
import icons.FuDocIcons;

import javax.swing.*;
import java.awt.*;

/**
 * 将控制台注册到Run窗体上
 *
 * @author wangdingfu
 * @date 2023-07-25 15:41:40
 */
public class FuConsoleManager {


    public static void registerRunWindow(Project project, ConsoleView consoleView) {
        UIUtil.invokeLaterIfNeeded(() -> {
            JPanel consolePanel = new JPanel(new BorderLayout());
            JComponent component = consoleView.getComponent();
            consolePanel.add(component, BorderLayout.CENTER);
            final RunContentDescriptor contentDescriptor = new RunContentDescriptor(consoleView, null, consolePanel, FuDocConstants.FU_DOC, FuDocIcons.FU_DOC);
            contentDescriptor.setFocusComputable(() -> component);
            contentDescriptor.setAutoFocusContent(true);
            RunContentManager.getInstance(project).showRunContent(DefaultRunExecutor.getRunExecutorInstance(), contentDescriptor);
        });
    }
}
