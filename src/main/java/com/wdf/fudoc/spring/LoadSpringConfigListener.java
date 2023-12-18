package com.wdf.fudoc.spring;

import com.intellij.openapi.project.DumbService;
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
        try {
            project.getMessageBus().connect().subscribe(DumbService.DUMB_MODE, new DumbService.DumbModeListener() {
                @Override
                public void exitDumbMode() {
                    log.info("开始加载Spring环境信息...");
                    SpringBootEnvLoader.doLoad(project, false,false);
                }
            });
        } catch (Exception e) {
            log.info("读取SpringBoot配置文件异常");
        }
    }


}
