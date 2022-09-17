package com.wdf.fudoc.test.config;

import com.google.common.collect.Lists;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2022-08-25 10:49:42
 */
@Getter
public class ModuleSpringConfig {

    /**
     * 当前module下所有的配置文件集合
     */
    private final List<SpringConfigFile> configFileList;

    /**
     * 当前module默认环境
     */
    private String defaultEnv;

    /**
     * 当前module下所有环境
     */
    private final List<String> envList = Lists.newArrayList();

    /**
     * 每一个环境对应的配置文件
     */
    private final Map<String, SpringConfigFile> envConfigMap = new ConcurrentHashMap<>();

    /**
     * 主配置文件
     */
    private SpringConfigFile mainConfigFile;


    public ModuleSpringConfig(List<SpringConfigFile> springConfigFileList) {
        this.configFileList = springConfigFileList;
        for (SpringConfigFile springConfigFile : springConfigFileList) {
            String fileName = springConfigFile.getFileName();
            String env = StringUtils.substringBetween(fileName, SpringConfigFileConstants.SPLIT, ".");
            if (StringUtils.isBlank(env)) {
                env = SpringConfigFileConstants.DEFAULT_ENV;
                this.mainConfigFile = springConfigFile;
            }
            envList.add(env);
            envConfigMap.put(env, springConfigFile);
        }
        //从主配置文件中读取环境
        if (Objects.nonNull(this.mainConfigFile)) {
            String env = this.mainConfigFile.getConfigValue(SpringConfigFileConstants.ENV_KEY);
            this.defaultEnv = StringUtils.isBlank(env) ? SpringConfigFileConstants.DEFAULT_ENV : env;
        }
    }


    /**
     * 从配置文件中获取属性
     *
     * @param key 属性key
     * @return 属性对应的值
     */
    public String getAttr(String key) {
        //1、先取对应环境的配置文件
        SpringConfigFile springConfigFile = envConfigMap.get(defaultEnv);
        if (Objects.nonNull(springConfigFile)) {
            String configValue = springConfigFile.getConfigValue(key);
            if (StringUtils.isNotBlank(configValue)) {
                return configValue;
            }
        }
        //2、没有获取到则去主配置文件获取
        if (Objects.nonNull(this.mainConfigFile)) {
            return this.mainConfigFile.getConfigValue(key);
        }
        return StringUtils.EMPTY;
    }


}
