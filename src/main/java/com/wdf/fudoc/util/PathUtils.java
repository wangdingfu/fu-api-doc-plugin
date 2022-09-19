package com.wdf.fudoc.util;

import com.google.common.collect.Lists;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @descption: 路径拼接工具类
 * @date 2022-05-31 01:46:28
 */
public class PathUtils {

    private final static String JOINT = "/";

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
     * url请求路径拼接
     *
     * @param urls 多个url路径
     * @return 拼接成最终的url
     */
    public static String urlJoin(String... urls) {
        return JOINT + joinUrl(urls);
    }

    /**
     * url请求路径拼接
     *
     * @param urls 多个url路径
     * @return 拼接成最终的url
     */
    public static String joinUrl(String... urls) {
        if (Objects.isNull(urls) || urls.length <= 0) {
            return StringUtils.EMPTY;
        }
        List<String> uriList = Lists.newArrayList();
        for (String uri : urls) {
            String[] split = StringUtils.split(uri, JOINT);
            if (Objects.nonNull(split) && split.length > 0) {
                for (String s : split) {
                    s = s.replaceAll(JOINT, "");
                    if (StringUtils.isNotBlank(s) && !JOINT.equals(s)) {
                        uriList.add(s);
                    }
                }
            }
        }
        return StringUtils.join(uriList, JOINT);
    }
}
