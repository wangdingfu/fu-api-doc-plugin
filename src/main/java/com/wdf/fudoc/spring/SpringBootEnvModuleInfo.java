package com.wdf.fudoc.spring;

import com.intellij.openapi.module.Module;
import com.wdf.fudoc.request.pojo.SpringBootEnvConfigInfo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.apache.commons.collections.CollectionUtils;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * @author wangdingfu
 * @date 2023-07-22 17:30:38
 */
public class SpringBootEnvModuleInfo {

    /**
     * springBoot信息
     */
    @Getter
    private final Map<Module, SpringBootEnvInfo> envMap = new ConcurrentHashMap<>();

    /**
     * 是否加载了配置
     */
    @Getter
    @Setter
    private boolean isLoad;

    public Set<String> getApplication() {
        return envMap.values().stream().map(SpringBootEnvInfo::getApplicationName).collect(Collectors.toSet());
    }


    public SpringBootEnvInfo getEnvInfo(Module module) {
        SpringBootEnvInfo springBootEnvInfo = envMap.get(module);
        if (Objects.nonNull(springBootEnvInfo)) {
            return springBootEnvInfo;
        }
        for (SpringBootEnvInfo value : envMap.values()) {
            List<Module> childList = value.getChildList();
            if (CollectionUtils.isNotEmpty(childList) && childList.contains(module)) {
                return value;
            }
        }
        return null;
    }


    public void addEnvInfo(Module module, String application, String defaultEnv, Map<String, SpringBootEnvConfigInfo> envConfigInfoMap, List<Module> childList) {
        envMap.put(module, new SpringBootEnvInfo(application, defaultEnv, envConfigInfoMap, childList));
    }


    @Getter
    @Setter
    @AllArgsConstructor
    public static class SpringBootEnvInfo {

        /**
         * springboot启动类名称
         */
        private String applicationName;

        /**
         * 默认环境
         */
        private String defaultEnv;

        /**
         * 端口号信息 key：环境 value：端口号
         */
        private Map<String, SpringBootEnvConfigInfo> envConfigInfoMap;

        /**
         * 子module
         */
        private List<Module> childList;

    }
}
