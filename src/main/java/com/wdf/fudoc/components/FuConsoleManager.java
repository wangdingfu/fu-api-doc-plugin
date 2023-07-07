package com.wdf.fudoc.components;

import com.intellij.openapi.project.Project;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-07 11:20:48
 */
public class FuConsoleManager {

    /**
     * 存放本次动作的一些全局上下文数据
     */
    private static final ThreadLocal<FuConsole> FU_CONSOLE_THREAD_LOCAL = new ThreadLocal<>();


    public static FuConsole get(Project project) {
        FuConsole fuConsole = FU_CONSOLE_THREAD_LOCAL.get();
        if (Objects.isNull(fuConsole)) {
            fuConsole = new FuConsole(project);
            FU_CONSOLE_THREAD_LOCAL.set(fuConsole);
        }
        return fuConsole;
    }
}
