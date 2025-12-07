package com.wdf.fudoc.util;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-11-23 23:13:43
 */
public class HttpResponseUtil {

    /**
     * 从响应头中获取指定 header 的值（忽略大小写）
     *
     * @param headers    响应头Map
     * @param headerName header名称
     * @return header值列表，找不到返回空列表
     */
    public static List<String> getHeaderIgnoreCase(Map<String, List<String>> headers, String headerName) {
        if (headers == null || headers.isEmpty() || FuStringUtils.isBlank(headerName)) {
            return Collections.emptyList();
        }

        // 先精确匹配
        List<String> values = headers.get(headerName);
        if (values != null && !values.isEmpty()) {
            return values;
        }

        // 忽略大小写查找
        for (Map.Entry<String, List<String>> entry : headers.entrySet()) {
            if (headerName.equalsIgnoreCase(entry.getKey())) {
                return entry.getValue() != null ? entry.getValue() : Collections.emptyList();
            }
        }

        return Collections.emptyList();
    }

    /**
     * 从响应头中获取指定 header 的第一个值（忽略大小写）
     *
     * @param headers    响应头Map
     * @param headerName header名称
     * @return header值，找不到返回null
     */
    public static String getFirstHeaderIgnoreCase(Map<String, List<String>> headers, String headerName) {
        List<String> values = getHeaderIgnoreCase(headers, headerName);
        return values.isEmpty() ? null : values.get(0);
    }


    public static String getFileNameFromDisposition(HttpResponse httpResponse) {
        return Objects.isNull(httpResponse) ? FuStringUtils.EMPTY : readFileName(httpResponse.header(Header.CONTENT_DISPOSITION));
    }

    /**
     * 从Content-Disposition头中获取文件名
     *
     * @return 文件名，empty表示无
     */
    public static String readFileName(String contentDisposition) {
        if (FuStringUtils.isBlank(contentDisposition)) {
            return FuStringUtils.EMPTY;
        }
        //默认就用utf-8编码解码
        contentDisposition = URLUtil.decode(contentDisposition);
        String[] split = contentDisposition.split(";");
        String fileName = FuStringUtils.EMPTY;
        for (String str : split) {
            if (str.startsWith("filename=")) {
                fileName = ReUtil.get("filename=\"(.*?)\"", str, 1);
                if (StrUtil.isBlank(fileName)) {
                    fileName = StrUtil.subAfter(str, "filename=", true);
                }
            }
            if (str.startsWith("filename*=")) {
                //优先级最高 直接返回文件名称
                return StrUtil.subAfter(str, "filename*=", true).replace("utf-8''", "").replace("UTF-8''", "");
            }
        }
        return fileName;
    }


}
