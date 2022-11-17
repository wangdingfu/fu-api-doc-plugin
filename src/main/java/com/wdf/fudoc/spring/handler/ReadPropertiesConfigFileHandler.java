package com.wdf.fudoc.spring.handler;

import com.intellij.openapi.vfs.VirtualFile;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.Properties;

/**
 * @author wangdingfu
 * @date 2022-08-23 23:08:13
 */
@Slf4j
public class ReadPropertiesConfigFileHandler extends AbstractReadConfigFileHandler {

    private final Properties properties;

    public ReadPropertiesConfigFileHandler(VirtualFile virtualFile) {
        this.properties = new Properties();
        try {
            this.properties.load(readFile(virtualFile));
        } catch (IOException e) {
            log.info("加载properties配置文件失败", e);
        }
    }

    @Override
    public String getAttr(String key) {
        return this.properties.getProperty(key);
    }
}
