package com.wdf.fudoc.spring;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.vfs.VirtualFile;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.yaml.snakeyaml.Yaml;

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

    /**
     * 当前激活的环境
     */
    private String activeEnv;

    /**
     * 配置文件内容
     */
    private final Map<String, JSON> configMap = new ConcurrentHashMap<>();


    /**
     * 从配置文件中获取默认配置
     *
     * @param key 配置key
     * @return 配置的值
     */
    public String getConfig(String key) {
        String config = getConfig(configMap.get(activeEnv), key);
        if (StringUtils.isEmpty(config)) {
            for (JSON value : configMap.values()) {
                config = getConfig(value, key);
                if (StringUtils.isNotBlank(config)) {
                    return config;
                }
            }
        }
        return config;
    }


    private String getConfig(JSON json, String key) {
        Object value;
        if (Objects.nonNull(json) && Objects.nonNull(value = json.getByPath(key))) {
            return value.toString();
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
            readYamlConfigFile(name, inputStream);
        } else {
            //properties配置文件
            readPropertiesConfigFile(name, inputStream);
        }
    }


    public void readYamlConfigFile(String fileName, InputStream inputStream) {
        Yaml yaml = new Yaml();
        Iterable<Object> configIterable = yaml.loadAll(inputStream);
        for (Object object : configIterable) {
            addConfig(fileName, JSONUtil.parse(object));
        }
    }


    private void addConfig(String fileName, JSON config) {
        String env = StringUtils.substringBetween(fileName, SpringConfigFileConstants.SPLIT, ".");
        if (StringUtils.isBlank(env)) {
            Object active = config.getByPath(SpringConfigFileConstants.ENV_KEY);
            if (Objects.nonNull(active)) {
                this.activeEnv = active.toString();
            } else {
                Object profiles = config.getByPath(SpringConfigFileConstants.PROFILES);
                if (Objects.nonNull(profiles)) {
                    env = profiles.toString();
                }
            }
        }
        env = StringUtils.isBlank(env) ? SpringConfigFileConstants.DEFAULT_ENV : env;
        JSON json = configMap.get(env);
        if (Objects.isNull(json)) {
            configMap.put(env, config);
        }
    }


    public void readPropertiesConfigFile(String fileName, InputStream inputStream) {
        try {
            Properties properties = new Properties();
            properties.load(inputStream);
            addConfig(fileName, JSONUtil.parse(properties));
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
