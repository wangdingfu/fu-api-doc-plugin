package com.wdf.fudoc.test.action;

import com.intellij.execution.console.LanguageConsoleImpl;
import com.intellij.execution.console.LanguageConsoleView;
import com.intellij.execution.console.ProcessBackedConsoleExecuteActionHandler;
import com.intellij.execution.process.ColoredProcessHandler;
import com.intellij.execution.process.KillableProcessHandler;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.AbstractConsoleRunnerWithHistory;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import com.intellij.util.io.BaseOutputReader;
import lombok.SneakyThrows;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author wangdingfu
 * @date 2023-07-18 15:52:05
 */
public class LogConsoleRunner extends AbstractConsoleRunnerWithHistory<LanguageConsoleView> {

    private final Project project;
    private final Process logProcess;

    public LogConsoleRunner(@NotNull Project project, @NotNull @Nls(capitalization = Nls.Capitalization.Title) String consoleTitle, Process process) {
        super(project, consoleTitle, project.getBasePath());
        this.project = project;
        this.logProcess = process;
    }

    @Override
    protected LanguageConsoleView createConsoleView() {
        LanguageConsoleImpl consoleView = new LanguageConsoleImpl(project, "Bash", PlainTextLanguage.INSTANCE);
        consoleView.setEditable(false);
        return consoleView;
    }

    @Override
    protected @Nullable Process createProcess() {
        return this.logProcess;
    }

    @SneakyThrows
    @Override
    protected OSProcessHandler createProcessHandler(Process process) {
        return new MyColoredProcessHandler(process, "run " + "fu_api");
    }

    @Override
    protected @NotNull ProcessBackedConsoleExecuteActionHandler createExecuteActionHandler() {
        return new ProcessBackedConsoleExecuteActionHandler(getProcessHandler(), true);
    }



    public void close() {
        ProcessHandler processHandler = getProcessHandler();
        if (processHandler instanceof KillableProcessHandler killableProcessHandler) {
            killableProcessHandler.killProcess();
        } else {
            processHandler.destroyProcess();
        }

    }

    private static class MyColoredProcessHandler extends ColoredProcessHandler {
        public MyColoredProcessHandler(Process process, @NotNull String commandLine) {
            super(process, commandLine);
        }

        @Override
        protected BaseOutputReader.@NotNull Options readerOptions() {
            return BaseOutputReader.Options.forMostlySilentProcess();
        }
    }
}
