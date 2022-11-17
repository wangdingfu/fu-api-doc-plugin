package com.wdf.fudoc.spring.handler;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.vfs.VirtualFile;
import com.wdf.fudoc.spring.SpringConfigFileConstants;
import com.wdf.fudoc.spring.SpringConfigFileManager;
import org.apache.commons.lang3.StringUtils;
import org.yaml.snakeyaml.Yaml;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-08-23 23:06:54
 */
public class ReadYmlConfigFileHandler extends AbstractReadConfigFileHandler {

    private final String defaultEnv;

    private final Map<String, JSON> ymlConfigMap;

    public ReadYmlConfigFileHandler(VirtualFile virtualFile) {
        Yaml yaml = new Yaml();
        Iterable<Object> configIterable = yaml.loadAll(readFile(virtualFile));
        Iterator<Object> iterator = configIterable.iterator();
        ymlConfigMap = new HashMap<>();
        String env = null;
        while (iterator.hasNext()) {
            Object next = iterator.next();
            JSON config = Objects.nonNull(next) ? JSONUtil.parse(next) : new JSONObject();
            Object profiles = config.getByPath(SpringConfigFileConstants.PROFILES);
            Object defaultEnv = config.getByPath(SpringConfigFileConstants.ENV_KEY);
            if (Objects.nonNull(defaultEnv)) {
                env = defaultEnv.toString();
            }
            String key = SpringConfigFileConstants.DEFAULT_ENV;
            if (Objects.nonNull(profiles)) {
                key = profiles.toString();
            }
            ymlConfigMap.put(key, config);
        }
        this.defaultEnv = StringUtils.isNotBlank(env) ? env : SpringConfigFileConstants.DEFAULT_ENV;
    }

    @Override
    public String getAttr(String key) {
        JSON json = ymlConfigMap.get(this.defaultEnv);
        Object configValue = null;
        if (Objects.nonNull(json)) {
            configValue = json.getByPath(key);
        }
        return Objects.nonNull(configValue) ? configValue.toString() : "";
    }


}
