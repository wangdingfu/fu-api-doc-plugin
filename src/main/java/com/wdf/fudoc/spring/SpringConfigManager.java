package com.wdf.fudoc.spring;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.roots.ModuleRootManager;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.newvfs.BulkFileListener;
import com.intellij.openapi.vfs.newvfs.events.VFileEvent;
import com.wdf.fudoc.util.FuStringUtils;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * spring配置文件管理
 *
 * @author wangdingfu
 * @date 2022-11-19 13:48:16
 */
@Slf4j
public class SpringConfigManager {

    private static final Map<Module, SpringConfigFile> MODULE_SPRING_CONFIG_MAP = new ConcurrentHashMap<>();

    /**
     * 配置文件最后修改时间缓存，用于判断是否需要重新加载
     */
    private static final Map<Module, Long> CONFIG_LAST_MODIFIED_MAP = new ConcurrentHashMap<>();

    /**
     * 文件监听器是否已注册
     */
    private static volatile boolean listenerRegistered = false;

    /**
     * 注册文件变更监听器（使用消息总线）
     */
    public static void registerFileListener() {
        if (listenerRegistered) {
            return;
        }
        synchronized (SpringConfigManager.class) {
            if (listenerRegistered) {
                return;
            }
            // 使用消息总线订阅文件变更事件
            ApplicationManager.getApplication().getMessageBus()
                    .connect()
                    .subscribe(VirtualFileManager.VFS_CHANGES, new BulkFileListener() {
                        @Override
                        public void after(@NotNull List<? extends VFileEvent> events) {
                            for (VFileEvent event : events) {
                                VirtualFile file = event.getFile();
                                if (file != null && isSpringConfigFile(file)) {
                                    invalidateCacheForFile(file);
                                }
                            }
                        }
                    });
            listenerRegistered = true;
            log.debug("Spring配置文件监听器已注册");
        }
    }

    /**
     * 判断是否是 Spring 配置文件
     */
    private static boolean isSpringConfigFile(VirtualFile file) {
        if (file == null || file.isDirectory()) {
            return false;
        }
        String extension = file.getExtension();
        if (!SpringConfigFileConstants.EXTENSIONS.contains(extension)) {
            return false;
        }
        String fileName = FuStringUtils.substringBeforeLast(file.getName(), ".");
        String baseName = fileName.contains(SpringConfigFileConstants.SPLIT)
                ? FuStringUtils.substringBeforeLast(fileName, SpringConfigFileConstants.SPLIT)
                : fileName;
        return SpringConfigFileConstants.CONFIG_FILE_NAMES.contains(baseName);
    }

    /**
     * 使指定文件相关的缓存失效
     */
    private static void invalidateCacheForFile(VirtualFile file) {
        MODULE_SPRING_CONFIG_MAP.entrySet().removeIf(entry -> {
            Module module = entry.getKey();
            VirtualFile resourceDir = getResourceDir(module);
            if (resourceDir != null && file.getPath().startsWith(resourceDir.getPath())) {
                CONFIG_LAST_MODIFIED_MAP.remove(module);
                log.debug("清除模块 {} 的配置缓存，触发文件: {}", module.getName(), file.getName());
                return true;
            }
            return false;
        });
    }

    /**
     * 手动清除指定模块的配置缓存
     */
    public static void invalidateCache(Module module) {
        if (module != null) {
            MODULE_SPRING_CONFIG_MAP.remove(module);
            CONFIG_LAST_MODIFIED_MAP.remove(module);
            log.debug("手动清除模块 {} 的配置缓存", module.getName());
        }
    }

    /**
     * 清除所有缓存
     */
    public static void invalidateAllCache() {
        MODULE_SPRING_CONFIG_MAP.clear();
        CONFIG_LAST_MODIFIED_MAP.clear();
        log.debug("清除所有配置缓存");
    }


    /**
     * 从spring配置文件中读取当前模块的端口
     *
     * @param module 所属的java模块
     * @return 当前项目的端口
     */
    public static Integer getServerPort(Module module) {
        String configValue = getConfigValue(module, SpringConfigFileConstants.SERVER_PORT_KEY);
        if (FuStringUtils.isBlank(configValue)) {
            return SpringConfigFileConstants.DEFAULT_SERVER_PORT;
        }

        try {
            return parsePort(configValue);
        } catch (Exception e) {
            log.warn("解析端口配置失败: {}", configValue);
            return SpringConfigFileConstants.DEFAULT_SERVER_PORT;
        }
    }

    /**
     * 解析端口配置，支持多种格式
     */
    private static Integer parsePort(String portValue) {
        portValue = portValue.trim();

        // 处理 ${} 占位符格式
        if (portValue.startsWith("${") && portValue.endsWith("}")) {
            String innerValue = portValue.substring(2, portValue.length() - 1);
            String[] parts = innerValue.split(":");
            if (parts.length > 1) {
                portValue = parts[1].trim();
            } else {
                return SpringConfigFileConstants.DEFAULT_SERVER_PORT;
            }
        }

        // 处理随机端口配置
        if ("0".equals(portValue) || "random".equalsIgnoreCase(portValue)) {
            return SpringConfigFileConstants.DEFAULT_SERVER_PORT;
        }

        // 处理范围表达式
        if (portValue.contains("-")) {
            portValue = portValue.split("-")[0].trim();
        }

        int port = Integer.parseInt(portValue);
        if (port < 1 || port > 65535) {
            return SpringConfigFileConstants.DEFAULT_SERVER_PORT;
        }

        return port;
    }

    public static String getContextPath(Module module) {
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
     * 初始化指定模块下的配置文件（带缓存检查）
     */
    public static SpringConfigFile initSpringConfig(Module module) {
        // 注册文件监听器
        registerFileListener();

        // 检查缓存是否需要更新
        if (isCacheValid(module)) {
            return MODULE_SPRING_CONFIG_MAP.get(module);
        }

        // 缓存失效，重新加载
        return MODULE_SPRING_CONFIG_MAP.compute(module, (m, old) -> {
            SpringConfigFile config = doLoadSpringConfig(m);
            updateLastModified(m);
            return config;
        });
    }

    /**
     * 检查缓存是否有效
     */
    private static boolean isCacheValid(Module module) {
        if (!MODULE_SPRING_CONFIG_MAP.containsKey(module)) {
            return false;
        }

        Long lastModified = CONFIG_LAST_MODIFIED_MAP.get(module);
        if (lastModified == null) {
            return false;
        }

        // 检查配置文件是否有更新
        VirtualFile resourceDir = getResourceDir(module);
        if (resourceDir == null) {
            return false;
        }

        long currentMaxModified = getMaxModificationTime(resourceDir);
        return currentMaxModified <= lastModified;
    }

    /**
     * 获取资源目录下配置文件的最大修改时间
     */
    private static long getMaxModificationTime(VirtualFile resourceDir) {
        long maxTime = 0;
        for (VirtualFile child : resourceDir.getChildren()) {
            if (!child.isDirectory() && isSpringConfigFile(child)) {
                maxTime = Math.max(maxTime, child.getTimeStamp());
            }
        }
        return maxTime;
    }

    /**
     * 更新最后修改时间
     */
    private static void updateLastModified(Module module) {
        VirtualFile resourceDir = getResourceDir(module);
        if (resourceDir != null) {
            CONFIG_LAST_MODIFIED_MAP.put(module, getMaxModificationTime(resourceDir));
        }
    }


    public static SpringConfigFile doLoadSpringConfig(Module module) {
        SpringConfigFile springConfigFile = new SpringConfigFile();
        VirtualFile resourceDir = getResourceDir(module);

        if (Objects.nonNull(resourceDir)) {
            List<VirtualFile> mainConfigFiles = new ArrayList<>();
            List<VirtualFile> envConfigFiles = new ArrayList<>();

            for (VirtualFile child : resourceDir.getChildren()) {
                if (!child.isDirectory() && SpringConfigFileConstants.EXTENSIONS.contains(child.getExtension())) {
                    String originalFileName = child.getName();
                    String configFileName = FuStringUtils.substringBeforeLast(originalFileName, ".");

                    boolean hasEnvSuffix = configFileName.contains(SpringConfigFileConstants.SPLIT);
                    String baseName = hasEnvSuffix
                            ? FuStringUtils.substringBeforeLast(configFileName, SpringConfigFileConstants.SPLIT)
                            : configFileName;

                    if (SpringConfigFileConstants.CONFIG_FILE_NAMES.contains(baseName)) {
                        if (!hasEnvSuffix) {
                            mainConfigFiles.add(child);
                        } else {
                            envConfigFiles.add(child);
                        }
                    }
                }
            }

            // 先加载主配置文件
            for (VirtualFile mainFile : mainConfigFiles) {
                springConfigFile.addConfigFile(mainFile);
            }

            // 后加载环境特定配置文件
            for (VirtualFile envFile : envConfigFiles) {
                springConfigFile.addConfigFile(envFile);
            }

            Set<String> envs = springConfigFile.getEnvs();
            if (envs.isEmpty()) {
                log.debug("模块 {} 未找到Spring配置文件", module.getName());
            }
        }

        springConfigFile.setModule(module);
        return springConfigFile;
    }


    /**
     * 获取指定java module下resource的目录
     */
    private static VirtualFile getResourceDir(Module module) {
        if (module == null) {
            return null;
        }
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
