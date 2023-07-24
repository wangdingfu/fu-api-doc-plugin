package com.wdf.fudoc.console;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.console.LanguageConsoleImpl;
import com.intellij.execution.console.LanguageConsoleView;
import com.intellij.execution.console.ProcessBackedConsoleExecuteActionHandler;
import com.intellij.execution.process.KillableProcessHandler;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.AbstractConsoleRunnerWithHistory;
import com.intellij.openapi.fileTypes.PlainTextLanguage;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.common.constant.FuDocConstants;
import icons.FuDocIcons;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import javax.swing.*;

/**
 * @author wangdingfu
 * @date 2023-07-24 19:44:55
 */
public class FuConsoleRunner extends AbstractConsoleRunnerWithHistory<LanguageConsoleView> {

    private final Project project;

    private final PipedProcess logProcess;

    public FuConsoleRunner(@NotNull Project project, PipedProcess logProcess) {
        super(project, FuDocConstants.FU_DOC, project.getBasePath());
        this.project = project;
        this.logProcess = logProcess;
    }

    @Override
    protected LanguageConsoleView createConsoleView() {
        LanguageConsoleImpl consoleView = new LanguageConsoleImpl(this.project, "", PlainTextLanguage.INSTANCE);
        consoleView.setEditable(false);
        return consoleView;
    }


    @Override
    protected @Nullable Icon getConsoleIcon() {
        return FuDocIcons.FU_DOC;
    }

    @Override
    protected @Nullable Process createProcess() throws ExecutionException {
        return this.logProcess;
    }

    @Override
    protected OSProcessHandler createProcessHandler(Process process) {
        return new FuColoredProcessHandler(process, "");
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
}
