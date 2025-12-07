package com.wdf.fudoc.spring.handler;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Properties配置文件处理器
 *
 * @author wangdingfu
 * @date 2022-11-23 21:18:12
 */
@Slf4j
public class PropertiesConfigFileHandler extends AbstractConfigFileHandler {

    /**
     * 存储配置的Map
     */
    private final Map<String, String> configMap = new LinkedHashMap<>();

    public PropertiesConfigFileHandler(Properties properties) {
        propertiesToMap(properties);
    }

    public PropertiesConfigFileHandler(InputStream inputStream) {
        parseInputStream(inputStream);
    }

    /**
     * 解析输入流
     */
    private void parseInputStream(InputStream inputStream) {
        if (inputStream == null) {
            log.warn("Properties输入流为null");
            return;
        }

        try {
            byte[] bytes = IoUtil.readBytes(inputStream);
            if (bytes == null || bytes.length == 0) {
                log.warn("Properties文件内容为空");
                return;
            }

            String content = new String(bytes, StandardCharsets.ISO_8859_1);
            log.debug("解析Properties配置，内容长度: {}", content.length());

            // 预处理占位符
            content = preprocessPlaceholders(content);

            // 加载Properties
            Properties properties = new Properties();
            try (InputStreamReader reader = new InputStreamReader(
                    new ByteArrayInputStream(content.getBytes(StandardCharsets.ISO_8859_1)),
                    StandardCharsets.ISO_8859_1)) {
                properties.load(reader);
            }

            propertiesToMap(properties);
            log.debug("Properties解析完成，共{}个配置项", configMap.size());

        } catch (IOException e) {
            log.error("读取Properties配置文件异常", e);
        }
    }

    /**
     * 将Properties转换为Map
     */
    private void propertiesToMap(Properties properties) {
        if (properties == null) {
            return;
        }
        for (String name : properties.stringPropertyNames()) {
            String value = properties.getProperty(name);
            if (value != null) {
                configMap.put(name, value);
            }
        }
    }

    @Override
    protected String doGetConfig(String key) {
        return configMap.get(key);
    }

    @Override
    public Map<String, String> getAllConfig() {
        return processPlaceholders(configMap);
    }
}
