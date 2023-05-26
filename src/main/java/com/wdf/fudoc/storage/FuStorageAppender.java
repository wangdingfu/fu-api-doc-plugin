package com.wdf.fudoc.storage;


import cn.hutool.core.io.FileUtil;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtilRt;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.common.exception.FuDocException;
import com.wdf.fudoc.util.ProjectUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.nio.charset.Charset;
import java.util.List;

/**
 * @author wangdingfu
 * @date 2023-05-26 15:53:10
 */
@Slf4j
public class FuStorageAppender {


    private final File file;

    public FuStorageAppender(File file) {
        this.file = file;
    }

    public static FuStorageAppender getInstance(Project project, String fileName, String path) {
        File file = FileUtil.file(project.getBasePath(), FuDocConstants.IDEA_DIR, FuDocConstants.FU_DOC, path, fileName);
        if (FileUtilRt.createIfNotExists(file)) {
            log.error("创建目录【{}】失败", file.getAbsolutePath());
        }
        if (!file.isFile()) {
            throw new FuDocException("创建文件异常");
        }
        return new FuStorageAppender(file);
    }

    /**
     * 向指定文件追加内容
     *
     * @param content 追加内容
     */
    public void append(String content) {
        try {
            FileUtil.appendUtf8String(content, this.file);
        } catch (Exception e) {
            log.error("向文件{}追加内容{}失败", file.getAbsolutePath(), content, e);
        }
    }

    /**
     * 读取文件 按行读取
     */
    public List<String> read() {
        return FileUtil.readLines(this.file, Charset.defaultCharset());
    }
}
