package com.wdf.fudoc.test.config;

import cn.hutool.core.thread.ThreadUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.startup.StartupActivity;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;


/**
 * 加载spring配置文件监听器
 *
 * @author wangdingfu
 * @date 2022-08-23 21:28:17
 */
@Slf4j
public class LoadSpringConfigListener implements StartupActivity.Background {


    /**
     * 启动的时候初始化当前项目下所有模块的spring配置文件到内存中
     *
     * @param project 当前项目
     */
    @Override
    public void runActivity(@NotNull Project project) {
        ThreadUtil.execAsync(() -> SpringConfigFileManager.initProjectSpringConfig(project));
    }


}
