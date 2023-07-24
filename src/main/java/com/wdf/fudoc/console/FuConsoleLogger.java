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

    private final AtomicBoolean close = new AtomicBoolean(false);

    public FuConsoleLogger(Project project) {
        this.project = project;
    }

    @Override
    protected void log(String logContent) {
        if (StringUtils.isBlank(logContent)) {
            return;
        }
        if (StringUtils.isNotBlank(prefix)) {
            logContent = "[" + prefix + "] " + logContent;
        }
        PipedProcess pipedProcess = getProcess();
        if (Objects.isNull(pipedProcess)) {
            return;
        }
        try {
            pipedProcess.getOutForInputStream().write(logContent.getBytes(StandardCharsets.UTF_8));
        } catch (Exception e) {
            LOGGER.error("打印日志【" + logContent + "】异常", e);
        }
    }


    public void close() {
        close.set(true);
        if (Objects.nonNull(process)) {
            process.setExitValue(0);
            fuConsoleRunner.close();
            process = null;
            fuConsoleRunner = null;
        }
    }


    private PipedProcess getProcess() {
        if (Objects.isNull(process) || Objects.isNull(fuConsoleRunner)) {
            try {
                if (lock.tryLock(3, TimeUnit.SECONDS)) {
                    if (close.get()) {
                        return null;
                    }
                    if (process == null) {
                        process = new PipedProcess();
                    }
                    if (fuConsoleRunner == null) {
                        try {
                            fuConsoleRunner = new FuConsoleRunner(project, process);
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
