package com.wdf.fudoc.spring.config.handler;

/**
 * @author wangdingfu
 * @date 2022-08-23 23:05:22
 */
public interface ReadSpringConfigFileHandler {

    /**
     * 根据key获取配置文件的值
     *
     * @param key 配置文件key
     * @return 对应的value
     */
    String getAttr(String key);
}
