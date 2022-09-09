package com.wdf.fudoc.request.spring.config;

import com.wdf.fudoc.request.spring.config.handler.ReadSpringConfigFileHandler;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * @author wangdingfu
 * @date 2022-08-23 22:46:21
 */
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class SpringConfigFile {

    /**
     * 文件名称
     */
    private String fileName;

    /**
     * 文件扩展名
     */
    private String extension;

    /**
     * 文件内容
     */
    private ReadSpringConfigFileHandler handler;


    public String getConfigValue(String configKey){
        return this.handler.getAttr(configKey);
    }
}
