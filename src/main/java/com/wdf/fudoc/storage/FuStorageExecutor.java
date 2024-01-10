package com.wdf.fudoc.storage;

import cn.hutool.core.io.FileUtil;
import com.intellij.openapi.project.Project;
import com.wdf.fudoc.common.FuDocRender;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.navigation.recent.RecentNavigationManager;
import com.wdf.fudoc.request.http.convert.HttpDataConvert;
import com.wdf.fudoc.request.pojo.FuHttpRequestData;
import com.wdf.api.util.JsonUtil;
import com.wdf.api.util.ProjectUtils;
import lombok.extern.slf4j.Slf4j;
import com.wdf.fudoc.util.FuStringUtils;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author wangdingfu
 * @date 2023-05-09 20:34:28
 */
@Slf4j
public class FuStorageExecutor {

    public static final String FU_DOC_DIR = "fudoc";
    public static final String FU_DOC_CONFIG = "config";
    public static final String FU_DOC_API = "request";
    public static final String FU_DOC_API_SUFFIX = ".http";





    /**
     * fuDoc存储初始化
     */
    public static void init(Project project) {
        //初始化fudoc目录
        initHome(project);

    }


    private static void initHome(Project project) {
        //检查是否存在.fudoc目录 没有则创建
        String currentProjectPath = project.getBasePath();
        if (FuStringUtils.isNotBlank(currentProjectPath)) {
            File file = FileUtil.file(currentProjectPath, FuDocConstants.IDEA_DIR, FU_DOC_DIR);
            if (file.exists()) {
                return;
            }
            if (file.mkdir()
                    && FileUtil.file(currentProjectPath, FuDocConstants.IDEA_DIR, FU_DOC_DIR, FU_DOC_CONFIG).mkdir()
                    && FileUtil.file(currentProjectPath, FuDocConstants.IDEA_DIR, FU_DOC_DIR, FU_DOC_API).mkdir()) {
                log.info("初始化[fudoc]根目录成功");
            }
        }
    }

    public static void saveRequest(FuHttpRequestData fuHttpRequestData) {
        String apiName = fuHttpRequestData.getApiName();
        String currentProjectPath = ProjectUtils.getCurrentProjectPath();
        String httpFileContent = FuDocRender.httpRender(HttpDataConvert.convert(fuHttpRequestData));
        File file = FileUtil.file(currentProjectPath,FuDocConstants.IDEA_DIR, FU_DOC_DIR, FU_DOC_API, apiName + FU_DOC_API_SUFFIX);
        saveFile(file, httpFileContent);
    }

    public static void saveFile(File file, String text) {
        if (file.exists()) {
            //存在则删除该文件
            FileUtil.del(file);
        }
        FileUtil.writeString(text, file, Charset.defaultCharset());
    }


    public static FuHttpRequestData readFile(String apiName) {
        String currentProjectPath = ProjectUtils.getCurrentProjectPath();
        File file = FileUtil.file(currentProjectPath, FU_DOC_DIR, FU_DOC_API, apiName + FU_DOC_API_SUFFIX);
        if (file.exists()) {
            String httpFileContent = FuStringUtils.toEncodedString(FileUtil.readBytes(file), StandardCharsets.UTF_8);
            if (FuStringUtils.isNotBlank(httpFileContent)) {
                return JsonUtil.toBean(httpFileContent, FuHttpRequestData.class);
            }
        }
        return null;
    }
}
