package com.wdf.fudoc.spring.handler;

import cn.hutool.json.JSON;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.intellij.openapi.vfs.VirtualFile;
import org.yaml.snakeyaml.Yaml;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-08-23 23:06:54
 */
public class ReadYmlConfigFileHandler extends AbstractReadConfigFileHandler {

    private final JSON ymlConfig;

    public ReadYmlConfigFileHandler(VirtualFile virtualFile) {
        Yaml yaml = new Yaml();
        Object object = yaml.load(readFile(virtualFile));
        this.ymlConfig = Objects.nonNull(object) ? JSONUtil.parse(object) : new JSONObject();
    }

    @Override
    public String getAttr(String key) {
        Object configValue = this.ymlConfig.getByPath(key);
        return Objects.nonNull(configValue) ? configValue.toString() : "";
    }
}
