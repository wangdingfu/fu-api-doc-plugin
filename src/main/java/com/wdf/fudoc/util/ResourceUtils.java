package com.wdf.fudoc.util;

import cn.hutool.core.io.resource.ClassPathResource;

/**
 * @author wangdingfu
 * @date 2022-08-07 12:42:49
 */
public class ResourceUtils {


    /**
     * 读取resources目录下的文件
     *
     * @param path resources目录内的路径
     * @return 文件内容
     */
    public static String readResource(String path) {
        return new ClassPathResource(path, ResourceUtils.class.getClassLoader()).readUtf8Str();
    }
}
