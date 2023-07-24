package com.wdf.fudoc.test.action;

import cn.hutool.core.util.CharsetUtil;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.console.PipedProcess;

/**
 * @author wangdingfu
 * @date 2023-07-24 11:03:06
 */
public class ConsoleRunnerLogger {


    private final PipedProcess pipedProcess;

    public ConsoleRunnerLogger(Project project) {
        pipedProcess = new PipedProcess();
        try {
            LogConsoleRunner logConsoleRunner = new LogConsoleRunner(project, "FuApi", pipedProcess);
            logConsoleRunner.initAndRun();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void log(String logInfo) {
        if (logInfo == null) return;
        try {
            pipedProcess.getOutForInputStream().write(logInfo.getBytes(CharsetUtil.defaultCharset()));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
