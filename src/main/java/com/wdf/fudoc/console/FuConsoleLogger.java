package com.wdf.fudoc.console;

import cn.hutool.core.text.StrFormatter;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.wdf.fudoc.common.constant.FuDocConstants;
import lombok.Getter;
import lombok.Setter;
import com.wdf.fudoc.util.FuStringUtils;

import javax.swing.*;
import java.util.Objects;

/**
 * 控制台输出日志
 *
 * @author wangdingfu
 * @date 2023-07-25 10:22:00
 */
public class FuConsoleLogger implements FuLogger {

    @Getter
    private final ConsoleView consoleView;

    @Getter
    @Setter
    private String prefix;

    public boolean isEmpty() {
        return Objects.isNull(this.consoleView);
    }

    public FuConsoleLogger(ConsoleView consoleView) {
        this.consoleView = consoleView;
    }


    @Override
    public void infoLog(String console, Object... params) {
        this.log(StrFormatter.format(console, params), ConsoleViewContentType.NORMAL_OUTPUT);
    }

    @Override
    public void errorLog(String console, Object... params) {
        this.log(StrFormatter.format(console, params), ConsoleViewContentType.ERROR_OUTPUT);
    }

    @Override
    public void debugLog(String console, Object... params) {
        this.log(StrFormatter.format(console, params), ConsoleViewContentType.LOG_DEBUG_OUTPUT);
    }

    @Override
    public void println() {
        this.consoleView.print(FuDocConstants.LINE, ConsoleViewContentType.NORMAL_OUTPUT);
    }

    @Override
    public void close() {
        consoleView.clear();
        consoleView.dispose();
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
        if (FuStringUtils.isNotBlank(this.prefix)) {
            this.consoleView.print("[" + this.prefix + "] ", ConsoleViewContentType.LOG_DEBUG_OUTPUT);
        }
        this.consoleView.print(info, contentType);
    }


    public void clear() {
        if (Objects.nonNull(this.consoleView)) {
            this.consoleView.clear();
        }
    }


    public JComponent getComponent() {
        if (Objects.nonNull(this.consoleView)) {
            return this.consoleView.getComponent();
        }
        return null;
    }
}
