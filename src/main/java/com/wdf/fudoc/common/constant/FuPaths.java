package com.wdf.fudoc.common.constant;

import com.intellij.openapi.application.PathManager;

import java.nio.file.Paths;

/**
 * @author wangdingfu
 * @date 2023-06-06 20:52:43
 */
public interface FuPaths {
    String EXTENSIONS = "extensions";

    String BASE_PATH = Paths.get(PathManager.getConfigPath(), EXTENSIONS).toString();

    String FU_DOC = "fudoc";

    String CONFIG = "config";

    String AUTH = "auth";

    String AUTH_PATH = Paths.get(BASE_PATH, FU_DOC, CONFIG, AUTH).toString();


    String FILE_NAME_PACKAGE = "package";


    String FILE_SUFFIX_JSON = ".json";


    String PACKAGE = FILE_NAME_PACKAGE + FILE_SUFFIX_JSON;

}
