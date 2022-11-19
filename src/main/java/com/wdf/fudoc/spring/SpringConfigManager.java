package com.wdf.fudoc.spring;

import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
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
     * 初始化执行项目下所有的spring配置文件到内存中
     *
     * @param project 当前项目
     */
    public static void initProjectSpringConfig(Project project) {
        Collection<Module> modules = ModuleUtil.getModulesOfType(project, JavaModuleType.getModuleType());
        for (Module module : modules) {
            MODULE_SPRING_CONFIG_MAP.put(module, initSpringConfig(module));
        }
    }


    /**
     * 从spring配置文件中读取当前模块的端口
     *
     * @param module 所属的java模块
     * @return 当前项目的端口
     */
    public static Integer getServerPort(Module module) {
        String configValue = getConfigValue(module, SpringConfigFileConstants.SERVER_PORT_KEY);
        return StringUtils.isBlank(configValue) ? SpringConfigFileConstants.DEFAULT_SERVER_PORT : Integer.parseInt(configValue);
    }


    public static String getConfigValue(Module module, String configKey) {
        SpringConfigFile springConfigFile = MODULE_SPRING_CONFIG_MAP.get(module);
        if (Objects.isNull(springConfigFile)) {
            //初始化当前模块的配置文件到内存中
            springConfigFile = initSpringConfig(module);
            MODULE_SPRING_CONFIG_MAP.put(module, springConfigFile);
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
        SpringConfigFile springConfigFile = new SpringConfigFile();
        //获取当前模块下的resource目录
        VirtualFile resourceDir = getResourceDir(module);
        if (Objects.nonNull(resourceDir)) {
            //获取resource目录下所有的文件
            for (VirtualFile child : resourceDir.getChildren()) {
                if (!child.isDirectory() && SpringConfigFileConstants.EXTENSIONS.contains(child.getExtension())) {
                    String configFileName = StringUtils.substringBeforeLast(child.getName(), ".");
                    if (configFileName.contains(SpringConfigFileConstants.SPLIT)) {
                        configFileName = StringUtils.substringBeforeLast(configFileName, SpringConfigFileConstants.SPLIT);
                    }
                    if (SpringConfigFileConstants.CONFIG_FILE_NAMES.contains(configFileName)) {
                        //配置文件
                        springConfigFile.addConfigFile(child);
                    }
                }
            }
        }
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


}