package com.wdf.fudoc.spring;

import com.google.common.collect.Sets;
import com.intellij.openapi.module.Module;
import com.intellij.openapi.vfs.VirtualFile;
import com.wdf.fudoc.spring.handler.ConfigFileHandler;
import com.wdf.fudoc.spring.handler.PropertiesConfigFileHandler;
import com.wdf.fudoc.spring.handler.YamlConfigFileHandler;
import com.wdf.fudoc.util.MavenUtils;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2022-11-19 13:55:50
 */
@Slf4j
public class SpringConfigFile {

    @Setter
    private Module module;

    /**
     * 当前激活的环境
     */
    @Getter
    private String activeEnv = SpringConfigFileConstants.DEFAULT_ENV;

    /**
     * 配置文件内容
     */
    private final Map<String, ConfigFileHandler> configMap = new ConcurrentHashMap<>();

    /**
     * 合并后的配置处理器
     */
    private MergedConfigFileHandler mergedConfig;


    public Set<String> getEnvs() {
        if (Objects.nonNull(mergedConfig)) {
            return Sets.newHashSet(mergedConfig.getEnvs());
        }
        Set<String> envs = Sets.newHashSet(configMap.keySet());
        envs.add(SpringConfigFileConstants.DEFAULT_ENV);
        return envs;
    }

    /**
     * 从配置文件中获取配置（使用合并后的配置）
     *
     * @param key 配置key
     * @return 配置的值
     */
    public String getConfig(String key) {
        String env = getDefaultEnv();

        // 优先使用合并后的配置
        if (mergedConfig != null) {
            return mergedConfig.getConfig(env, key);
        }

        // 兜底：从 configMap 读取
        ConfigFileHandler envConfig = configMap.get(env);
        String config = getConfig(envConfig, key);

        if (FuStringUtils.isEmpty(config)) {
            ConfigFileHandler defaultConfig = configMap.get(SpringConfigFileConstants.DEFAULT_ENV);
            config = getConfig(defaultConfig, key);
        }

        return FuStringUtils.isBlank(config) ? FuStringUtils.EMPTY : config;
    }


    public Integer getServerPort(String env) {
        String targetEnv = FuStringUtils.isNotBlank(env) ? env : getDefaultEnv();

        String config;
        if (Objects.nonNull(mergedConfig)) {
            config = mergedConfig.getConfig(targetEnv, SpringConfigFileConstants.SERVER_PORT_KEY);
        } else {
            config = getConfig(SpringConfigFileConstants.SERVER_PORT_KEY);
        }

        if (FuStringUtils.isNotBlank(config)) {
            try {
                // 处理 ${server.port:8080} 格式
                if (config.startsWith("${") && config.endsWith("}")) {
                    String inner = config.substring(2, config.length() - 1);
                    String[] parts = inner.split(":");
                    if (parts.length > 1) {
                        config = parts[1].trim();
                    }
                }

                if (FuStringUtils.isNumeric(config)) {
                    return Integer.parseInt(config);
                }
            } catch (NumberFormatException e) {
                log.warn("解析端口配置失败: {}", config);
            }
        }

        return SpringConfigFileConstants.DEFAULT_SERVER_PORT;
    }


    public String getConfig(String env, String key) {
        // 优先使用合并后的配置
        if (Objects.nonNull(mergedConfig)) {
            return mergedConfig.getConfig(env, key);
        }

        // 退回到旧逻辑（未初始化合并配置的兜底）
        ConfigFileHandler envConfig = configMap.get(env);
        String config = getConfig(envConfig, key);

        if (FuStringUtils.isEmpty(config)) {
            ConfigFileHandler defaultConfig = configMap.get(SpringConfigFileConstants.DEFAULT_ENV);
            config = getConfig(defaultConfig, key);
        }
        return config;
    }

    private String getDefaultEnv() {
        if (SpringConfigFileConstants.MAVEN_PROFILES.equals(this.activeEnv)) {
            // @profiles.active@ 场景处理 - 从maven中获取当前激活的环境
            try {
                List<String> activeProfiles = MavenUtils.getActiveProfiles(module);
                if (CollectionUtils.isNotEmpty(activeProfiles)) {
                    // 优先查找配置中存在的环境
                    for (String profile : activeProfiles) {
                        if (configMap.containsKey(profile)) {
                            return profile;
                        }
                    }
                    // 如果都不存在，使用第一个profile
                    return activeProfiles.get(0);
                }
            } catch (Exception e) {
                log.error("获取Maven profiles失败", e);
            }
        }

        // 如果activeEnv为空或不存在，尝试从配置文件中查找第一个可用环境
        if (FuStringUtils.isBlank(activeEnv) || !configMap.containsKey(activeEnv)) {
            if (!configMap.isEmpty()) {
                // 优先查找application环境
                if (configMap.containsKey(SpringConfigFileConstants.DEFAULT_ENV)) {
                    activeEnv = SpringConfigFileConstants.DEFAULT_ENV;
                } else {
                    // 否则使用第一个环境
                    activeEnv = configMap.keySet().iterator().next();
                }
            }
        }

        return activeEnv;
    }


    private String getConfig(ConfigFileHandler configFileHandler, String key) {
        if (Objects.nonNull(configFileHandler)) {
            return configFileHandler.getConfig(key);
        }
        return FuStringUtils.EMPTY;
    }

    public void addConfigFile(VirtualFile virtualFile) {
        InputStream inputStream = readFile(virtualFile);
        if (Objects.isNull(inputStream)) {
            return;
        }
        String extension = virtualFile.getExtension();
        String name = virtualFile.getName();

        if (SpringConfigFileConstants.YAML.equals(extension) || SpringConfigFileConstants.YML.equals(extension)) {
            //yml配置文件
            addConfig(name, new YamlConfigFileHandler(inputStream));
        } else {
            //properties配置文件
            readPropertiesConfigFile(name, inputStream);
        }
    }


    private void addConfig(String fileName, ConfigFileHandler config) {
        String env = FuStringUtils.substringBetween(fileName, SpringConfigFileConstants.SPLIT, ".");

        // 初始化合并配置处理器
        if (mergedConfig == null) {
            mergedConfig = new MergedConfigFileHandler();
        }

        // 将配置转换为Map
        Map<String, String> configValues;
        try {
            configValues = config.getAllConfig();
            if (configValues == null || configValues.isEmpty()) {
                return;
            }
        } catch (Exception e) {
            log.warn("从配置文件 {} 读取配置失败", fileName);
            return;
        }

        // 判断是否是主配置文件（无环境后缀）
        boolean isMainConfigFile = false;
        if (!fileName.contains(SpringConfigFileConstants.SPLIT)) {
            String baseName = FuStringUtils.substringBeforeLast(fileName, ".");
            if (SpringConfigFileConstants.CONFIG_FILE_NAMES.contains(baseName)) {
                isMainConfigFile = true;
            }
        }

        // 处理主配置文件
        if (isMainConfigFile) {
            mergedConfig.addBaseConfig(fileName, configValues);

            // 从主配置文件中读取激活的环境
            String activeEnv = configValues.get("spring.profiles.active");
            if (FuStringUtils.isNotBlank(activeEnv)) {
                // 处理多环境配置（逗号分隔）
                if (activeEnv.contains(",")) {
                    this.activeEnv = activeEnv.split(",")[0].trim();
                } else {
                    this.activeEnv = activeEnv.trim();
                }
            }

            // 保留主配置到默认环境
            configMap.put(SpringConfigFileConstants.DEFAULT_ENV, config);
            return;
        }

        // 处理环境特定配置文件
        if (FuStringUtils.isNotBlank(env)) {
            mergedConfig.addEnvConfig(env, configValues);
            configMap.put(env, config);
        } else {
            configMap.put(SpringConfigFileConstants.DEFAULT_ENV, config);
        }
    }


    public void readPropertiesConfigFile(String fileName, InputStream inputStream) {
        try {
            // 直接使用InputStream创建PropertiesConfigFileHandler
            addConfig(fileName, new PropertiesConfigFileHandler(inputStream));
        } catch (Exception e) {
            log.error("读取properties配置文件失败: {}", fileName, e);
        }
    }


    protected InputStream readFile(VirtualFile virtualFile) {
        try {
            return virtualFile.getInputStream();
        } catch (IOException e) {
            log.info("读取配置文件失败");
        }
        return null;
    }

}
