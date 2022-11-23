package com.wdf.fudoc.spring.handler;

import cn.hutool.json.JSON;
import org.apache.commons.lang3.StringUtils;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-11-23 21:20:11
 */
public class YamlConfigFileHandler implements ConfigFileHandler {

    private final JSON config;

    public YamlConfigFileHandler(JSON config) {
        this.config = config;
    }

    @Override
    public String getConfig(String key) {
        if (Objects.nonNull(this.config) && StringUtils.isNotBlank(key)) {
            Object value = this.config.getByPath(key);
            return Objects.nonNull(value) ? value.toString() : StringUtils.EMPTY;
        }
        return StringUtils.EMPTY;
    }
}
