package com.wdf.fudoc.spring.config;

import com.google.common.collect.Lists;
import com.intellij.openapi.module.JavaModuleType;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.module.ModuleUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.wdf.fudoc.spring.config.handler.ReadPropertiesConfigFileHandler;
import com.wdf.fudoc.spring.config.handler.ReadSpringConfigFileHandler;
import com.wdf.fudoc.spring.config.handler.ReadYmlConfigFileHandler;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * spring配置文件管理器
 *
 * @author wangdingfu
 * @date 2022-08-25 10:48:25
 */
public class SpringConfigFileManager {

    private static final Map<Module, ModuleSpringConfig> MODULE_SPRING_CONFIG_MAP = new ConcurrentHashMap<>();


    /**
     * 初始化执行项目下所有的spring配置文件到内存中
     *
     * @param project 当前项目
     */
    public static void initProjectSpringConfig(Project project) {
        Collection<Module> modules = ModuleUtil.getModulesOfType(project, JavaModuleType.getModuleType());
        for (Module module : modules) {
            ModuleSpringConfig moduleSpringConfig = initModuleConfig(module);
            if (Objects.nonNull(moduleSpringConfig)) {
                MODULE_SPRING_CONFIG_MAP.put(module, moduleSpringConfig);
            }
        }
    }

    /**
     * 获取配置
     *
     * @param module 所属模块
     * @param key    配置key
     * @return 配置的值
     */
    public static String getConfig(Module module, String key) {
        if (Objects.nonNull(module) && StringUtils.isNotBlank(key)) {
            ModuleSpringConfig moduleSpringConfig = MODULE_SPRING_CONFIG_MAP.get(module);
            if (Objects.nonNull(moduleSpringConfig)) {
                return moduleSpringConfig.getAttr(key);
            }
        }
        return StringUtils.EMPTY;
    }


    /**
     * 读取指定module下的端口
     *
     * @param module 指定java module
     * @return 当前module下项目启动的端口号
     */
    public static Integer getServerPort(Module module) {
        String config = getConfig(module, SpringConfigFileConstants.SERVER_PORT_KEY);
        if (StringUtils.isNotBlank(config) && StringUtils.isNumeric(config)) {
            return Integer.parseInt(config);
        }
        return SpringConfigFileConstants.DEFAULT_SERVER_PORT;
    }


    /**
     * 初始化指定module下所有的配置文件到内存中
     *
     * @param module java module
     * @return 指定module下所有的spring项目配置文件
     */
    private static ModuleSpringConfig initModuleConfig(Module module) {
        //获取当前模块下的resource目录
        VirtualFile resourceDir = getResourceDir(module);
        if (Objects.nonNull(resourceDir)) {
            List<SpringConfigFile> springConfigFileList = Lists.newArrayList();
            //获取resource目录下所有的文件
            for (VirtualFile child : resourceDir.getChildren()) {
                if (!child.isDirectory() && SpringConfigFileConstants.EXTENSIONS.contains(child.getExtension())) {
                    String configFileName = StringUtils.substringBeforeLast(child.getName(), ".");
                    if (configFileName.contains(SpringConfigFileConstants.SPLIT)) {
                        configFileName = StringUtils.substringBeforeLast(configFileName, SpringConfigFileConstants.SPLIT);
                    }
                    if (SpringConfigFileConstants.CONFIG_FILE_NAMES.contains(configFileName)) {
                        //配置文件
                        springConfigFileList.add(convertSpringConfigFile(child));
                    }
                }
            }
            if (CollectionUtils.isNotEmpty(springConfigFileList)) {
                return new ModuleSpringConfig(springConfigFileList);
            }
        }
        return null;
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


    /**
     * 将文件对象转为spring配置文件对象
     *
     * @param virtualFile 文件对象
     * @return spring配置文件对象
     */
    private static SpringConfigFile convertSpringConfigFile(VirtualFile virtualFile) {
        String extension = virtualFile.getExtension();
        String name = virtualFile.getName();
        ReadSpringConfigFileHandler configFileHandler = (SpringConfigFileConstants.YAML.equals(extension) || SpringConfigFileConstants.YML.equals(extension)) ? new ReadYmlConfigFileHandler(virtualFile) : new ReadPropertiesConfigFileHandler(virtualFile);
        return new SpringConfigFile(name, extension, configFileHandler);
    }

}
