package com.wdf.fudoc.spring.handler;

import java.util.List;
import java.util.Map;

/**
 * @author wangdingfu
 * @date 2022-11-23 21:17:44
 */
public interface ConfigFileHandler {

    /**
     * 获取指定配置的值
     */
    String getConfig(String key);

    /**
     * 获取所有配置
     */
    Map<String, String> getAllConfig();


    /**
     * 获取当前配置中其他环境的所有配置
     */
    default Map<String, Map<String, String>> getOtherEnvConfig() {
        //仅yaml 需要考虑实现
        return null;
    }
}
