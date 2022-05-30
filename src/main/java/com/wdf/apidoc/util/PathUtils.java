package com.wdf.apidoc.util;

import java.io.File;

/**
 * @author wangdingfu
 * @descption: 路径拼接工具类
 * @date 2022-05-31 01:46:28
 */
public class PathUtils {

    /**
     * 文件系统路径 拼接
     *
     * @param rootDir 根目录
     * @param subs    子目录集合
     * @return 最终的目录
     */
    public static String filePathJoin(String rootDir, String... subs) {

        if (subs == null || subs.length == 0) {
            return rootDir;
        }
        StringBuilder path = new StringBuilder(rootDir);
        for (String sub : subs) {
            String s = sub;
            //若子目录前面也带了斜杠则去掉
            if (s.startsWith("/")) {
                s = s.substring(1);
            }

            if (path.toString().endsWith(File.separator) || path.toString().endsWith("/")) {
                path.append(s);
            } else {
                path.append(File.separator).append(s);
            }
        }
        return path.toString();
    }

    /**
     * http路径拼接
     *
     * @param uri  url地址
     * @param subs 拼接的url段集合
     * @return 完整的url地址
     */
    public static String httpPathJoin(String uri, String... subs) {
        if (subs == null || subs.length == 0) {
            return uri;
        }
        StringBuilder path = new StringBuilder(uri);
        for (String sub : subs) {
            String s = sub;
            //若子目录前面也带了斜杠则去掉
            if (s.startsWith("/")) {
                s = s.substring(1);
            }
            //拼接
            if (path.toString().endsWith("/")) {
                path.append(s);
            } else {
                path.append("/").append(s);
            }
        }
        return path.toString();
    }

}
