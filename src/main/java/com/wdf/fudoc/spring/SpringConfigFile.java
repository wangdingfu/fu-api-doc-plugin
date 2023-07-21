package com.wdf.fudoc.spring;

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
import org.apache.commons.lang.StringUtils;

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


    public Set<String> getEnvs() {
        return configMap.keySet();
    }

    /**
     * 从配置文件中获取默认配置
     *
     * @param key 配置key
     * @return 配置的值
     */
    public String getConfig(String key) {
        String env = getDefaultEnv();
        //第一步 优先从当前激活的环境中获取
        String config = getConfig(configMap.get(env), key);
        if (StringUtils.isEmpty(config)) {
            //第二步 从默认环境中获取
            return getConfig(configMap.get(SpringConfigFileConstants.DEFAULT_ENV), key);
        }
        return config;
    }


    public String getConfig(String env, String key) {
        //第一步 优先从当前激活的环境中获取
        String config = getConfig(configMap.get(env), key);
        if (StringUtils.isEmpty(config)) {
            //第二步 从默认环境中获取
            config = getConfig(configMap.get(getDefaultEnv()), key);
            if (StringUtils.isBlank(config)) {
                return SpringConfigFileConstants.DEFAULT_SERVER_PORT + "";
            }
        }
        return config;
    }

    private String getDefaultEnv() {
        if (SpringConfigFileConstants.MAVEN_PROFILES.equals(this.activeEnv)) {
            //issues #6 @profiles.active@ 场景处理 从maven中获取当前激活的环境
            List<String> activeProfiles = MavenUtils.getActiveProfiles(module);
            if (CollectionUtils.isNotEmpty(activeProfiles)) {
                return activeProfiles.stream().filter(configMap::containsKey).findFirst().orElse(activeProfiles.get(0));
            }
        }
        return activeEnv;
    }


    private String getConfig(ConfigFileHandler configFileHandler, String key) {
        if (Objects.nonNull(configFileHandler)) {
            return configFileHandler.getConfig(key);
        }
        return StringUtils.EMPTY;
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
        String env = StringUtils.substringBetween(fileName, SpringConfigFileConstants.SPLIT, ".");
        if (StringUtils.isBlank(env)) {
            Object active = config.getConfig(SpringConfigFileConstants.ENV_KEY);
            if (Objects.nonNull(active)) {
                this.activeEnv = active.toString();
            } else {
                Object profiles = config.getConfig(SpringConfigFileConstants.PROFILES);
                if (Objects.nonNull(profiles)) {
                    env = profiles.toString();
                }
            }
        }
        env = StringUtils.isBlank(env) ? SpringConfigFileConstants.DEFAULT_ENV : env;
        ConfigFileHandler configFile = configMap.get(env);
        if (Objects.isNull(configFile)) {
            configMap.put(env, config);
        }
    }


    public void readPropertiesConfigFile(String fileName, InputStream inputStream) {
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            addConfig(fileName, new PropertiesConfigFileHandler(properties));
        } catch (Exception e) {
            log.info("读取properties配置文件失败");
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
