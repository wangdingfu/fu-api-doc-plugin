package com.wdf.fudoc.spring.handler;


import com.wdf.fudoc.util.FuStringUtils;

import java.util.Objects;
import java.util.Properties;

/**
 * @author wangdingfu
 * @date 2022-11-23 21:18:12
 */
public class PropertiesConfigFileHandler implements ConfigFileHandler {

    private final Properties properties;

    public PropertiesConfigFileHandler(Properties properties) {
        this.properties = properties;
    }

    @Override
    public String getConfig(String key) {
        if (Objects.nonNull(properties) && FuStringUtils.isNotBlank(key)) {
            return this.properties.getProperty(key);
        }
        return FuStringUtils.EMPTY;
    }
}
