package com.wdf.fudoc.util;

import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;

import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-11-23 23:13:43
 */
public class HttpResponseUtil {

    /**
     * 从Content-Disposition头中获取文件名
     *
     * @return 文件名，empty表示无
     */
    public static String getFileNameFromDisposition(HttpResponse httpResponse) {
        String fileName = null;
        if(Objects.nonNull(httpResponse)){
            final String disposition = httpResponse.header(Header.CONTENT_DISPOSITION);
            if (StrUtil.isNotBlank(disposition)) {
                fileName = ReUtil.get("filename=\"(.*?)\"", disposition, 1);
                if (StrUtil.isBlank(fileName)) {
                    fileName = StrUtil.subAfter(disposition, "filename=", true);
                }
            }
        }
        return fileName;
    }
}
