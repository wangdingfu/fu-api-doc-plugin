package com.wdf.fudoc.components;

import cn.hutool.core.text.StrFormatter;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.application.ModalityState;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.wm.ToolWindow;
import com.intellij.openapi.wm.ToolWindowManager;
import com.intellij.openapi.wm.ToolWindowType;
import com.intellij.ui.content.Content;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.swing.*;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-06 22:09:21
 */
public class FuConsole {

    @Getter
    private final ConsoleView consoleView;

    @Setter
    private String prefix;

    public FuConsole(Project project) {
        this.consoleView = getConsoleView(project);
        if (Objects.nonNull(this.consoleView)) {
            this.consoleView.clear();
        }
    }

    public FuConsole(ConsoleView consoleView) {
        this.consoleView = consoleView;
    }

    /**
     * 打印日志
     *
     * @param console 日志内容
     */
    public void error(String console, Object... params) {
        this.log(StrFormatter.format(console, params), ConsoleViewContentType.ERROR_OUTPUT);
        this.println();
    }

    public void println() {
        this.consoleView.print("\n", ConsoleViewContentType.NORMAL_OUTPUT);
    }

    public void infoLog(String console, Object... params) {
        this.log(StrFormatter.format(console, params), ConsoleViewContentType.NORMAL_OUTPUT);
    }

    public void info(String console, Object... params) {
        infoLog(console, params);
        this.println();
    }

    public void userInfo(String console, Object... params) {
        this.log(StrFormatter.format(console, params), ConsoleViewContentType.USER_INPUT);
    }

    public void verbose(String console, Object... params) {
        this.log(StrFormatter.format(console, params), ConsoleViewContentType.LOG_VERBOSE_OUTPUT);
        this.println();
    }

    public void debugLog(String console, Object... params) {
        this.log(StrFormatter.format(console, params), ConsoleViewContentType.LOG_DEBUG_OUTPUT);
    }

    public void debug(String console, Object... params) {
        debugLog(console, params);
        this.println();
    }

    public void warn(String console, Object... params) {
        this.log(StrFormatter.format(console, params), ConsoleViewContentType.LOG_WARNING_OUTPUT);
    }


    /**
     * 打印日志
     *
     * @param info        日志内容
     * @param contentType 内容渲染样式
     */
    public void log(String info, ConsoleViewContentType contentType) {
        if (Objects.isNull(this.consoleView)) {
            return;
        }
        if (StringUtils.isNotBlank(this.prefix)) {
            this.consoleView.print("[" + this.prefix + "] ", ConsoleViewContentType.LOG_DEBUG_OUTPUT);
        }
        this.consoleView.print(info, contentType);
    }


    public JComponent getComponent() {
        if (Objects.nonNull(this.consoleView)) {
            return this.consoleView.getComponent();
        }
        return null;
    }


    public void clear() {
        if (Objects.nonNull(this.consoleView)) {
            this.consoleView.clear();
        }
    }

    private ConsoleView getConsoleView(Project project) {
        ToolWindowManager toolWindowManager = ToolWindowManager.getInstance(project);
        ToolWindow toolWindow = toolWindowManager.getToolWindow("Fu Console");
        if (Objects.isNull(toolWindow)) {
            return null;
        }
        ApplicationManager.getApplication().invokeLater(() -> {
            toolWindow.activate(null);
            toolWindow.setType(ToolWindowType.DOCKED, null);
        }, ModalityState.any());
        Content content = toolWindow.getContentManager().getContent(0);
        if (Objects.isNull(content)) {
            return null;
        }
        return (ConsoleView) content.getComponent();
    }
}
