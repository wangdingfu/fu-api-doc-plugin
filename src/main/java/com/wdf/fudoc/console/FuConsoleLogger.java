package com.wdf.fudoc.console;

import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import org.apache.commons.lang.StringUtils;

import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author wangdingfu
 * @date 2023-07-24 20:28:45
 */
public class FuConsoleLogger extends BaseFuLogger {
    private static final Logger LOGGER = Logger.getInstance(FuConsoleLogger.class);

    private String prefix;

    private final Project project;

    private PipedProcess process;

    private FuConsoleRunner fuConsoleRunner;

    private final ReentrantLock lock = new ReentrantLock();

    private final String commandLine;

    public FuConsoleLogger(Project project, String commandLine) {
        this.project = project;
        this.commandLine = commandLine;
    }

    @Override
    protected void log(String logContent) {
        if (Objects.isNull(logContent)) {
            return;
        }
        PipedProcess pipedProcess = getProcess();
        if (Objects.isNull(pipedProcess)) {
            return;
        }
        if (StringUtils.isNotBlank(prefix)) {
            logContent = "[" + prefix + "] " + logContent;
        }
        logContent = logContent + "\r\n";
        try {
            pipedProcess.getOutForInputStream().write(logContent.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.error("打印日志【" + logContent + "】异常", e);
        }
    }


    public void close() {
        if (Objects.nonNull(process)) {
            try {
                //等待100ms在关闭
                Thread.sleep(100);
            } catch (Exception e) {
                LOGGER.info("关闭异常");
            }
            process.setExitValue(0);
            process = null;
            fuConsoleRunner = null;
        }
    }


    private PipedProcess getProcess() {
        if (Objects.isNull(process) || Objects.isNull(fuConsoleRunner)) {
            try {
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    if (process == null) {
                        process = new PipedProcess();
                    }
                    if (fuConsoleRunner == null) {
                        try {
                            fuConsoleRunner = new FuConsoleRunner(project, process, commandLine);
                            fuConsoleRunner.initAndRun();
                        } catch (Exception e) {
                            return null;
                        }
                    }
                }
            } catch (Exception e) {
                return null;
            } finally {
                lock.unlock();
            }
        }
        return process;
    }

    @Override
    public void setPrefix(String prefix) {
        this.prefix = prefix;
    }
}
