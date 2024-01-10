package com.wdf.fudoc.util;

import cn.hutool.core.net.URLDecoder;
import cn.hutool.core.util.ReUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.URLUtil;
import cn.hutool.http.Header;
import cn.hutool.http.HttpResponse;
import com.wdf.fudoc.util.FuStringUtils;

import java.util.List;
import java.util.Objects;

/**
 * @author wangdingfu
 * @date 2022-11-23 23:13:43
 */
public class HttpResponseUtil {


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
