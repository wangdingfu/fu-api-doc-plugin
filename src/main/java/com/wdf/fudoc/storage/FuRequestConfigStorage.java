package com.wdf.fudoc.storage;

import com.intellij.openapi.project.Project;
import com.wdf.fudoc.common.constant.FuPaths;
import com.wdf.fudoc.request.po.FuRequestConfigPO;
import com.wdf.fudoc.util.JsonUtil;
import com.wdf.fudoc.util.StorageUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;
import java.util.Objects;

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

    public FuRequestConfigStorage(Project project) {
        this.path = Paths.get(FuPaths.BASE_PATH, project.getName(), FuPaths.CONFIG).toString();
    }

    public static FuRequestConfigStorage getInstance(Project project) {
        return new FuRequestConfigStorage(project);
    }

    public FuRequestConfigPO readData() {
        if (Objects.nonNull(this.configPO)) {
            return this.configPO;
        }
        String content = StorageUtils.readContent(path, FILE_NAME);
        if (StringUtils.isNotBlank(content)) {
            this.configPO = JsonUtil.toBean(content, FuRequestConfigPO.class);
            return this.configPO;
        }
        return new FuRequestConfigPO();
    }


    public void saveData(FuRequestConfigPO configPO) {
        this.configPO = configPO;
        StorageUtils.writeJson(path, FILE_NAME, configPO);
    }

}
