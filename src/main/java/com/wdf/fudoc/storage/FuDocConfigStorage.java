package com.wdf.fudoc.storage;

import com.intellij.openapi.project.Project;
import com.wdf.fudoc.common.constant.FuPaths;
import com.wdf.fudoc.common.po.FuDocConfigPO;
import com.wdf.fudoc.util.JsonUtil;
import com.wdf.fudoc.util.StorageUtils;
import org.apache.commons.lang3.StringUtils;

import java.nio.file.Paths;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2023-07-08 22:42:47
 */
public class FuDocConfigStorage {

    public static final FuDocConfigStorage INSTANCE = new FuDocConfigStorage();

    private static final String FILE_NAME = "config.json";

    private final String path;

    private FuDocConfigPO configPO;


    public FuDocConfigStorage() {
        this.path = Paths.get(FuPaths.BASE_PATH, FuPaths.CONFIG).toString();
    }

    public static FuRequestConfigStorage getInstance(Project project) {
        return new FuRequestConfigStorage(project);
    }

    public FuDocConfigPO readData() {
        if (Objects.nonNull(this.configPO)) {
            return this.configPO;
        }
        String content = StorageUtils.readContent(path, FILE_NAME);
        if (StringUtils.isNotBlank(content)) {
            this.configPO = JsonUtil.toBean(content, FuDocConfigPO.class);
            return this.configPO;
        }
        return new FuDocConfigPO();
    }


    public void saveData(FuDocConfigPO configPO) {
        this.configPO = configPO;
        saveData();
    }


    public void saveData() {
        if (Objects.nonNull(this.configPO)) {
            StorageUtils.writeJson(path, FILE_NAME, configPO);
        }
    }
}
