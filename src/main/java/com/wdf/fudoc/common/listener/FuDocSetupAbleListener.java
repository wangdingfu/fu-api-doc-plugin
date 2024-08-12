package com.wdf.fudoc.common.listener;

import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import cn.fudoc.common.service.FuDocSetupAble;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ServiceLoader;

/**
 * @author wangdingfu
 * @date 2023-10-02 18:59:04
 */
@Slf4j
public class FuDocSetupAbleListener implements StartupActivity {

    @Override
    public void runActivity(@NotNull Project project) {
        ServiceLoader<FuDocSetupAble> load = ServiceLoader.load(FuDocSetupAble.class, FuDocSetupAbleListener.class.getClassLoader());
        load.forEach(f -> f.init(project));
    }
}
