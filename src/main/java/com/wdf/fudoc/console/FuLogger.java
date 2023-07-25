package com.wdf.fudoc.console;

import com.intellij.execution.ui.ConsoleView;

/**
 * @author wangdingfu
 * @date 2023-07-24 20:29:32
 */
public interface FuLogger {

    /**
     * 设置前缀
     *
     * @param prefix 前缀
     */
    void setPrefix(String prefix);

    String getPrefix();

    ConsoleView getConsoleView();

    boolean isEmpty();

    default void info(String console, Object... params) {
        infoLog(console, params);
        println();
    }

    default void error(String console, Object... params) {
        errorLog(console, params);
        println();
    }

    default void debug(String console, Object... params) {
        debugLog(console, params);
        println();
    }

    void clear();

    void infoLog(String console, Object... params);

    void errorLog(String console, Object... params);

    void debugLog(String console, Object... params);

    void println();

    void close();

}
