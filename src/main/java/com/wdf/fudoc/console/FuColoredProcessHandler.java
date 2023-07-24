package com.wdf.fudoc.console;

import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.util.io.BaseOutputReader;
import org.jetbrains.annotations.NotNull;

/**
 * @author wangdingfu
 * @date 2023-07-24 19:51:33
 */
public class FuColoredProcessHandler extends ColoredProcessHandler {
    public FuColoredProcessHandler(Process process, String commandLine) {
        super(process, commandLine);
    }

    @Override
    protected BaseOutputReader.@NotNull Options readerOptions() {
        return BaseOutputReader.Options.forMostlySilentProcess();
    }
}
