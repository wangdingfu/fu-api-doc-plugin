package com.wdf.fudoc.console;

/**
 * @author wangdingfu
 * @date 2023-07-24 20:29:32
 */
public interface FuLogger {

    void setPrefix(String prefix);


    void info(String console, Object... params);

    void error(String console, Object... params);

    void debug(String console, Object... params);

    void println();

}
