package com.wdf.fudoc.spring.handler;

import cn.hutool.core.io.IoUtil;
import lombok.extern.slf4j.Slf4j;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * YAML配置文件处理器
 *
 * @author wangdingfu
 * @date 2022-11-23 21:20:11
 */
@Slf4j
public class YamlConfigFileHandler extends AbstractConfigFileHandler {

    /**
     * 存储配置的扁平化Map
     */
    private final Map<String, Object> configMap = new LinkedHashMap<>();

    public YamlConfigFileHandler(InputStream inputStream) {
        parseInputStream(inputStream);
    }

    /**
     * 解析输入流
     */
    private void parseInputStream(InputStream inputStream) {
        if (inputStream == null) {
            log.warn("YAML输入流为null");
            return;
        }

        try {
            byte[] bytes = IoUtil.readBytes(inputStream);
            if (bytes == null || bytes.length == 0) {
                log.warn("YAML文件内容为空");
                return;
            }

            String content = new String(bytes, StandardCharsets.UTF_8);
            log.debug("解析YAML，内容长度: {}", content.length());

            // 预处理占位符
            content = preprocessPlaceholders(content);

            // 使用SnakeYAML解析
            Yaml yaml = new Yaml();
            Object yamlObject = yaml.load(content);

            if (yamlObject != null) {
                flattenYaml("", yamlObject, configMap);
                log.debug("YAML解析完成，共{}个配置项", configMap.size());
            } else {
                log.debug("YAML解析结果为null");
            }

        } catch (Exception e) {
            log.error("读取YAML配置文件异常", e);
        }
    }

    /**
     * 递归扁平化YAML对象
     */
    @SuppressWarnings("unchecked")
    private void flattenYaml(String prefix, Object obj, Map<String, Object> result) {
        if (obj == null) {
            return;
        }

        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
                Object value = entry.getValue();

                if (value instanceof Map) {
                    flattenYaml(key, value, result);
                } else if (value instanceof Iterable) {
                    handleArray(key, value, result);
                } else {
                    result.put(key, value);
                }
            }
        } else if (obj instanceof Iterable) {
            handleArray(prefix, obj, result);
        } else {
            result.put(prefix.isEmpty() ? "root" : prefix, obj);
        }
    }

    /**
     * 处理数组
     */
    private void handleArray(String prefix, Object array, Map<String, Object> result) {
        int index = 0;
        for (Object item : (Iterable<?>) array) {
            String key = prefix + "[" + index + "]";
            if (item instanceof Map || item instanceof Iterable) {
                flattenYaml(key, item, result);
            } else {
                result.put(key, item);
            }
            index++;
        }
    }

    @Override
    protected String doGetConfig(String key) {
        Object value = configMap.get(key);
        return value != null ? value.toString() : null;
    }

    @Override
    public Map<String, String> getAllConfig() {
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, Object> entry : configMap.entrySet()) {
            Object value = entry.getValue();
            if (value != null) {
                String valueStr = value.toString();
                result.put(entry.getKey(), resolvePlaceholder(valueStr));
            }
        }
        return result;
    }
}
