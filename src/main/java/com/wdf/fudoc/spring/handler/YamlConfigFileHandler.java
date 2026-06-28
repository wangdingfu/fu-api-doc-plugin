package com.wdf.fudoc.spring.handler;

import cn.hutool.core.io.IoUtil;
import com.wdf.fudoc.spring.SpringConfigFileConstants;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.MapUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

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

    private final Map<String, Map<String, String>> otherEnvConfig = new LinkedHashMap<>();


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
            Iterable<Object> objects = yaml.loadAll(content);
            objects.forEach(yamlObject -> {
                if (yamlObject != null) {
                    Map<String, Object> yamlConfigMap = flattenYaml("", yamlObject);
                    Object profiles = yamlConfigMap.get(SpringConfigFileConstants.PROFILES);
                    if(Objects.nonNull(profiles)){
                        otherEnvConfig.put(profiles.toString(),toConfigMap(yamlConfigMap));
                    }else {
                        configMap.putAll(yamlConfigMap);
                    }
                    log.debug("YAML解析完成，共{}个配置项", configMap.size());
                } else {
                    log.debug("YAML解析结果为null");
                }
            });
        } catch (Exception e) {
            log.info("读取YAML配置文件异常", e);
        }
    }

    /**
     * 递归扁平化YAML对象
     */
    @SuppressWarnings("unchecked")
    private Map<String, Object> flattenYaml(String prefix, Object obj) {
        if (obj == null) {
            return new LinkedHashMap<>();
        }
        Map<String, Object> result = new LinkedHashMap<>();
        if (obj instanceof Map) {
            Map<String, Object> map = (Map<String, Object>) obj;
            for (Map.Entry<String, Object> entry : map.entrySet()) {
                String key = prefix.isEmpty() ? entry.getKey() : prefix + "." + entry.getKey();
                Object value = entry.getValue();

                if (value instanceof Map) {
                    result.putAll(flattenYaml(key, value));
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
        return result;
    }

    /**
     * 处理数组
     */
    private void handleArray(String prefix, Object array, Map<String, Object> result) {
        int index = 0;
        for (Object item : (Iterable<?>) array) {
            String key = prefix + "[" + index + "]";
            if (item instanceof Map || item instanceof Iterable) {
                result.putAll(flattenYaml(key, item));
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
        return toConfigMap(configMap);
    }


    private Map<String, String> toConfigMap(Map<String, Object> configMap){
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


    @Override
    public Map<String, Map<String, String>> getOtherEnvConfig() {
        return otherEnvConfig;
    }
}
