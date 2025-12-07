package com.wdf.fudoc.spring.handler;

import com.wdf.fudoc.util.FuStringUtils;
import lombok.extern.slf4j.Slf4j;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 配置文件处理器抽象基类
 * 提取公共的占位符处理和配置获取逻辑
 *
 * @author wangdingfu
 * @date 2022-11-23 21:17:44
 */
@Slf4j
public abstract class AbstractConfigFileHandler implements ConfigFileHandler {

    /**
     * 存储 @@ 包裹的占位符内容
     */
    protected final Map<String, String> placeholderMap = new LinkedHashMap<>();

    /**
     * 预处理配置内容，处理 @xxx@ 格式的占位符
     *
     * @param content 原始配置内容
     * @return 处理后的内容
     */
    protected String preprocessPlaceholders(String content) {
        if (FuStringUtils.isBlank(content)) {
            return content;
        }

        String[] placeholders = FuStringUtils.substringsBetween(content, "@", "@");
        if (Objects.nonNull(placeholders)) {
            for (String placeholder : placeholders) {
                String originalStr = "@" + placeholder + "@";
                content = content.replace(originalStr, placeholder);
                placeholderMap.put(placeholder, originalStr);
            }
        }
        return content;
    }

    /**
     * 替换占位符为原始值
     *
     * @param value 配置值
     * @return 替换后的值
     */
    protected String resolvePlaceholder(String value) {
        if (value == null) {
            return FuStringUtils.EMPTY;
        }
        return placeholderMap.getOrDefault(value, value);
    }

    /**
     * 获取配置值的通用实现
     *
     * @param key 配置键
     * @return 配置值
     */
    @Override
    public String getConfig(String key) {
        if (FuStringUtils.isBlank(key)) {
            return FuStringUtils.EMPTY;
        }

        String value = doGetConfig(key);
        if (value == null) {
            return FuStringUtils.EMPTY;
        }
        return resolvePlaceholder(value);
    }

    /**
     * 子类实现的获取配置方法
     *
     * @param key 配置键
     * @return 配置值（可能为null）
     */
    protected abstract String doGetConfig(String key);

    /**
     * 处理配置Map中的占位符
     *
     * @param configMap 原始配置Map
     * @return 处理后的配置Map
     */
    protected Map<String, String> processPlaceholders(Map<String, String> configMap) {
        Map<String, String> result = new LinkedHashMap<>();
        for (Map.Entry<String, String> entry : configMap.entrySet()) {
            String value = entry.getValue();
            if (value != null) {
                result.put(entry.getKey(), resolvePlaceholder(value));
            }
        }
        return result;
    }
}
