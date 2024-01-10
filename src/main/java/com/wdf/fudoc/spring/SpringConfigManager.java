package com.wdf.fudoc.spring;

import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * spring配置文件管理
 *
 * @author wangdingfu
 * @date 2022-11-19 13:48:16
 */
public class SpringConfigManager {

    private static final Map<Module, SpringConfigFile> MODULE_SPRING_CONFIG_MAP = new ConcurrentHashMap<>();


    /**
     * 从spring配置文件中读取当前模块的端口
     *
     * @param module 所属的java模块
     * @return 当前项目的端口
     */
    public static Integer getServerPort(Module module) {
        String configValue = getConfigValue(module, SpringConfigFileConstants.SERVER_PORT_KEY);
        return FuStringUtils.isBlank(configValue) ? SpringConfigFileConstants.DEFAULT_SERVER_PORT : Integer.parseInt(configValue);
    }

    public static String getContextPath(Module module){
        return getConfigValue(module, SpringConfigFileConstants.CONTEXT_PATH_KEY);
    }


    public static String getConfigValue(Module module, String configKey) {
        SpringConfigFile springConfigFile = initSpringConfig(module);
        if (Objects.isNull(springConfigFile)) {
            return FuStringUtils.EMPTY;
        }
        return springConfigFile.getConfig(configKey);
    }


    /**
     * 初始化指定模块下的配置文件
     *
     * @param module 指定java模块
     * @return 初始化后的配置文件
     */
    public static SpringConfigFile initSpringConfig(Module module) {
        SpringConfigFile springConfigFile = MODULE_SPRING_CONFIG_MAP.get(module);
        if (Objects.isNull(springConfigFile)) {
            return doLoadSpringConfig(module);
        }
        return springConfigFile;
    }


    public static SpringConfigFile doLoadSpringConfig(Module module) {
        SpringConfigFile springConfigFile = new SpringConfigFile();
        //获取当前模块下的resource目录
        VirtualFile resourceDir = getResourceDir(module);
        if (Objects.nonNull(resourceDir)) {
            //获取resource目录下所有的文件
            for (VirtualFile child : resourceDir.getChildren()) {
                if (!child.isDirectory() && SpringConfigFileConstants.EXTENSIONS.contains(child.getExtension())) {
                    String configFileName = FuStringUtils.substringBeforeLast(child.getName(), ".");
                    if (configFileName.contains(SpringConfigFileConstants.SPLIT)) {
                        configFileName = FuStringUtils.substringBeforeLast(configFileName, SpringConfigFileConstants.SPLIT);
                    }
                    if (SpringConfigFileConstants.CONFIG_FILE_NAMES.contains(configFileName)) {
                        //配置文件
                        springConfigFile.addConfigFile(child);
                    }
                }
            }
        }
        springConfigFile.setModule(module);
        MODULE_SPRING_CONFIG_MAP.put(module, springConfigFile);
        return springConfigFile;
    }


    /**
     * 获取指定java module下resource的目录
     *
     * @param module java module
     * @return resource目录
     */
    private static VirtualFile getResourceDir(Module module) {
        VirtualFile[] rootList = ModuleRootManager.getInstance(module).getSourceRoots(false);
        for (VirtualFile virtualFile : rootList) {
            if (SpringConfigFileConstants.RESOURCE.equals(virtualFile.getName())) {
                return virtualFile;
            }
        }
        return null;
    }


    public static VirtualFile getFile(Module module, String fileName) {
        VirtualFile[] rootList = ModuleRootManager.getInstance(module).getSourceRoots(false);
        for (VirtualFile virtualFile : rootList) {
            if (SpringConfigFileConstants.RESOURCE.equals(virtualFile.getName())) {
                return virtualFile;
            }
        }
        return null;
    }


}
