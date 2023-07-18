package com.wdf.fudoc.test.action;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.console.LanguageConsoleImpl;
import com.intellij.execution.console.LanguageConsoleView;
import com.intellij.execution.console.ProcessBackedConsoleExecuteActionHandler;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.runners.AbstractConsoleRunnerWithHistory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.Nls;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 * @author wangdingfu
 * @date 2023-07-18 15:52:05
 */
public class LogConsoleRunner extends AbstractConsoleRunnerWithHistory<LanguageConsoleView> {

    private final Project project;

    public LogConsoleRunner(@NotNull Project project, @NotNull @Nls(capitalization = Nls.Capitalization.Title) String consoleTitle, @Nullable String workingDir) {
        super(project, consoleTitle, workingDir);
        this.project = project;
    }

    @Override
    protected LanguageConsoleView createConsoleView() {
        LanguageConsoleImpl consoleView = new LanguageConsoleImpl(project, "Bash", PlainTextLanguage.INSTANCE);
        consoleView.setEditable(false);
        return consoleView;
    }

    @Override
    protected @Nullable Process createProcess() throws ExecutionException {
        return null;
    }

    @Override
    protected OSProcessHandler createProcessHandler(Process process) {
        return null;
    }

    @Override
    protected @NotNull ProcessBackedConsoleExecuteActionHandler createExecuteActionHandler() {
        return null;
    }
}
