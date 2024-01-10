package com.wdf.fudoc.spring.handler;

import cn.hutool.core.io.IoUtil;
import cn.hutool.json.JSON;
import cn.hutool.json.JSONUtil;
import lombok.extern.slf4j.Slf4j;
import com.wdf.fudoc.util.FuStringUtils;
import org.yaml.snakeyaml.Yaml;

import java.io.InputStream;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author wangdingfu
 * @date 2022-11-23 21:20:11
 */
@Slf4j
public class YamlConfigFileHandler implements ConfigFileHandler {

    private JSON config;

    private final Map<String, String> itemMap = new ConcurrentHashMap<>();

    public YamlConfigFileHandler(InputStream inputStream) {
        try {
            Yaml yaml = new Yaml();
            String yamlContent = new String(IoUtil.readBytes(inputStream));
            //替换@@包裹的内容
            String[] itemList = FuStringUtils.substringsBetween(yamlContent, "@", "@");
            if (Objects.nonNull(itemList)) {
                for (String item : itemList) {
                    String itemStr = "@" + item + "@";
                    yamlContent = yamlContent.replace(itemStr, item);
                    itemMap.put(item, itemStr);
                }
            }
            Iterable<Object> objects = yaml.loadAll(yamlContent);
            this.config = JSONUtil.parse(objects.iterator().next());
        } catch (Exception e) {
            log.info("读取配置文件异常", e);
        }
    }

    @Override
    public String getConfig(String key) {
        if (Objects.isNull(this.config)) {
            return FuStringUtils.EMPTY;
        }
        String value = doGetConfig(key);
        if (FuStringUtils.isNotBlank(value)) {
            String itemValue = itemMap.get(value);
            if (FuStringUtils.isNotBlank(itemValue)) {
                return itemValue;
            }
        }
        return value;
    }


    private String doGetConfig(String key) {
        if (Objects.nonNull(this.config) && FuStringUtils.isNotBlank(key)) {
            Object value = this.config.getByPath(key);
            return Objects.nonNull(value) ? value.toString() : FuStringUtils.EMPTY;
        }
        return FuStringUtils.EMPTY;
    }
}
