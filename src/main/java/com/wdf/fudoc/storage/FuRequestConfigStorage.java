package com.wdf.fudoc.storage;

import com.intellij.openapi.project.Project;
import com.wdf.api.constants.FuPaths;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.api.util.JsonUtil;
import com.wdf.api.util.StorageUtils;
import com.wdf.fudoc.util.FuStringUtils;

import java.nio.file.Paths;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * [Fu Request]配置持久化
 * /fudoc/projectName/config/request-config.json
 * /fudoc/projectName/config/script/前置脚本.js
 *
 * @author wangdingfu
 * @date 2023-06-10 22:52:59
 */
public class FuRequestConfigStorage {

    private static final String FILE_NAME = "request-config.json";

    private final String path;

    private FuRequestConfigPO configPO;

    private FuRequestConfigStorage(Project project) {
        this.path = Paths.get(FuPaths.BASE_PATH, project.getName(), FuPaths.CONFIG).toString();
    }

    private static final Map<Project, FuRequestConfigStorage> storageMap = new ConcurrentHashMap<>();

    public static FuRequestConfigStorage get(Project project) {
        FuRequestConfigStorage fuRequestConfigStorage = storageMap.get(project);
        if (Objects.isNull(fuRequestConfigStorage)) {
            fuRequestConfigStorage = new FuRequestConfigStorage(project);
            storageMap.put(project, fuRequestConfigStorage);
        }
        return fuRequestConfigStorage;
    }

    public FuRequestConfigPO readData() {
        if (Objects.nonNull(this.configPO)) {
            return this.configPO;
        }
        String content = StorageUtils.readContent(path, FILE_NAME);
        this.configPO = FuStringUtils.isNotBlank(content) ? JsonUtil.toBean(content, FuRequestConfigPO.class) : new FuRequestConfigPO();
        return this.configPO;
    }


    public void saveData(FuRequestConfigPO configPO) {
        this.configPO = configPO;
        saveData();
    }


    public void saveData() {
        if (Objects.nonNull(this.configPO)) {
            StorageUtils.writeJson(path, FILE_NAME, configPO);
        }
    }

}
