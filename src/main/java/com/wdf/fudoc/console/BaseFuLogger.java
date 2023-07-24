package com.wdf.fudoc.console;

import cn.hutool.core.text.StrFormatter;

/**
 * @author wangdingfu
 * @date 2023-07-24 20:31:24
 */
public abstract class BaseFuLogger implements FuLogger {

    protected abstract void log(String logContent);


    @Override
    public void info(String console, Object... params) {
        log(StrFormatter.format(console, params));
    }


    @Override
    public void error(String console, Object... params) {
        log(StrFormatter.format(console, params));
        println();

    }


    @Override
    public void debug(String console, Object... params) {
        log(StrFormatter.format(console, params));
        println();
    }

    @Override
    public void println() {
        log("\r\n");

    }
}
