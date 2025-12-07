package com.wdf.fudoc.common.listener;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.ProjectActivity;
import cn.fudoc.common.service.FuDocSetupAble;
import kotlin.Unit;
import kotlin.coroutines.Continuation;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ServiceLoader;

/**
 * Fu Doc 项目初始化监听器
 * 从 StartupActivity 迁移到 ProjectActivity (IDEA 2022.3+ 推荐)
 *
 * @author wangdingfu
 * @date 2023-10-02 18:59:04
 */
@Slf4j
public class FuDocSetupAbleListener implements ProjectActivity {

    /**
     * 项目启动后执行
     * 这是新的 ProjectActivity API,在协程上下文中执行,不阻塞 UI 线程
     *
     * @param project      当前项目
     * @param continuation Kotlin 协程上下文 (Java 调用时可忽略)
     * @return null (Java 调用时返回 null 即可)
     */
    @Nullable
    @Override
    public Object execute(@NotNull Project project, @NotNull Continuation<? super Unit> continuation) {
        ServiceLoader<FuDocSetupAble> load = ServiceLoader.load(FuDocSetupAble.class, FuDocSetupAbleListener.class.getClassLoader());
        load.forEach(f -> f.init(project));
        return null;
    }
}
