package com.wdf.fudoc.spring.handler;

import com.intellij.openapi.vfs.VirtualFile;
import lombok.extern.slf4j.Slf4j;

import java.io.InputStream;

/**
 * @author wangdingfu
 * @date 2022-08-23 23:30:35
 */
@Slf4j
public abstract class AbstractReadConfigFileHandler implements ReadSpringConfigFileHandler {


    protected InputStream readFile(VirtualFile virtualFile) {
        try {
            return virtualFile.getInputStream();
        } catch (Exception e) {
            log.error("从项目中读取端口号失败", e);
        }
        return null;
    }
}
