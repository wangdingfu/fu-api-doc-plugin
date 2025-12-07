package com.wdf.fudoc.spring;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.MapUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * 合并后的配置文件处理器
 * 支持主配置(base) + 环境配置(env) 的合并模式
 *
 * @author wangdingfu
 * @date 2022-11-23 21:20:11
 */
@Slf4j
public class MergedConfigFileHandler {

    /**
     * 基础配置（来自主配置文件）
     */
    private final Map<String, String> baseConfig = new LinkedHashMap<>();

    /**
     * 环境特定配置
     */
    private final Map<String, Map<String, String>> envConfigs = new HashMap<>();

    /**
     * 合并后的配置缓存
     */
    private final Map<String, Map<String, String>> mergedConfigs = new HashMap<>();

    public MergedConfigFileHandler() {
        // 初始化默认环境
        mergedConfigs.put(SpringConfigFileConstants.DEFAULT_ENV, new LinkedHashMap<>());
    }

    /**
     * 添加主配置文件（base配置）
     *
     * @param configName 配置文件名
     * @param config 配置内容
     */
    public void addBaseConfig(String configName, Map<String, String> config) {
        try {
            if (config == null || config.isEmpty()) {
                log.debug("主配置 {} 为空，跳过", configName);
                return;
            }

            log.debug("添加主配置: {}", configName);
            baseConfig.putAll(config);
            updateAllMergedConfigs();
        } catch (Exception e) {
            log.error("添加主配置 {} 失败", configName, e);
        }
    }

    /**
     * 添加环境特定配置文件
     *
     * @param envName 环境名称
     * @param config 配置内容
     */
    public void addEnvConfig(String envName, Map<String, String> config) {
        try {
            if (envName == null || envName.isEmpty()) {
                log.warn("环境名称为空，跳过添加环境配置");
                return;
            }

            if (config == null || config.isEmpty()) {
                log.debug("环境 {} 配置为空，跳过", envName);
                return;
            }

            log.debug("添加环境配置: {}", envName);
            envConfigs.put(envName, config);
            updateMergedConfig(envName);
        } catch (Exception e) {
            log.error("添加环境配置 {} 失败", envName, e);
        }
    }

    /**
     * 获取配置值（优先从激活环境获取，然后从默认环境获取）
     *
     * @param env 环境名称
     * @param key 配置键
     * @return 配置值，获取失败返回空字符串
     */
    public String getConfig(String env, String key) {
        try {
            // 首先尝试从指定环境获取
            Map<String, String> mergedConfig = getMergedConfig(env);
            String value = mergedConfig.get(key);
            if (value != null) {
                return value;
            }

            // 如果指定环境没有，从默认环境获取
            mergedConfig = getMergedConfig(SpringConfigFileConstants.DEFAULT_ENV);
            value = mergedConfig.get(key);
            if (value != null) {
                return value;
            }
        } catch (Exception e) {
            log.warn("获取配置失败 env={}, key={}", env, key, e);
        }

        return "";
    }

    /**
     * 获取合并后的配置（带降级处理）
     *
     * @param env 环境名称
     * @return 合并后的配置Map，获取失败返回空Map
     */
    private Map<String, String> getMergedConfig(String env) {
        try {
            if (!mergedConfigs.containsKey(env)) {
                updateMergedConfig(env);
            }
            Map<String, String> config = mergedConfigs.get(env);
            return config != null ? config : Collections.emptyMap();
        } catch (Exception e) {
            log.warn("获取合并配置失败 env={}", env, e);
            return Collections.emptyMap();
        }
    }

    /**
     * 更新指定环境的合并配置
     * 优先级：环境配置 > 主配置(base)
     *
     * @param env 环境名称
     */
    private void updateMergedConfig(String env) {
        try {
            Map<String, String> merged = new LinkedHashMap<>();

            // 1. 先添加主配置作为基础
            merged.putAll(baseConfig);

            // 2. 再添加环境特定配置（会覆盖主配置中的相同项）
            Map<String, String> envConfig = envConfigs.get(env);
            if (MapUtils.isNotEmpty(envConfig)) {
                merged.putAll(envConfig);
            }

            // 缓存合并结果
            mergedConfigs.put(env, merged);
            log.debug("环境 {} 配置合并完成，共{}项", env, merged.size());
        } catch (Exception e) {
            log.error("更新环境 {} 的合并配置失败", env, e);
            // 降级：确保有一个空配置，避免NPE
            mergedConfigs.putIfAbsent(env, new LinkedHashMap<>());
        }
    }

    /**
     * 更新所有环境的合并配置
     */
    private void updateAllMergedConfigs() {
        try {
            // 更新默认环境
            updateMergedConfig(SpringConfigFileConstants.DEFAULT_ENV);

            // 更新所有环境
            for (String env : envConfigs.keySet()) {
                updateMergedConfig(env);
            }
        } catch (Exception e) {
            log.error("更新所有合并配置失败", e);
        }
    }

    /**
     * 获取所有可用的环境
     *
     * @return 环境名称集合
     */
    public Set<String> getEnvs() {
        try {
            Set<String> envs = new HashSet<>();
            envs.add(SpringConfigFileConstants.DEFAULT_ENV);
            envs.addAll(envConfigs.keySet());
            return envs;
        } catch (Exception e) {
            log.warn("获取环境列表失败", e);
            return Collections.singleton(SpringConfigFileConstants.DEFAULT_ENV);
        }
    }

    /**
     * 清除所有缓存（用于重新加载配置）
     */
    public void clear() {
        baseConfig.clear();
        envConfigs.clear();
        mergedConfigs.clear();
        mergedConfigs.put(SpringConfigFileConstants.DEFAULT_ENV, new LinkedHashMap<>());
    }
}
