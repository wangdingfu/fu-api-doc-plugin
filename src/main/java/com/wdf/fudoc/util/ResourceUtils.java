package com.wdf.fudoc.util;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.date.DateUtil;
import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.resource.ClassPathResource;
import cn.hutool.core.util.IdUtil;
import cn.hutool.system.SystemUtil;
import com.ibm.icu.util.LocaleData;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.util.FuStringUtils;

import java.io.File;
import java.time.LocalDateTime;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;


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


    /**
     * 创建需要保存文件的目录
     *
     * @param moduleName 当前所处module名称
     * @return 文件夹
     */
    public static File createFuRequestFileDir(String moduleName, String suffix) {
        String timeStr = DatePattern.PURE_DATE_FORMAT.format(new Date());
        String yyyyMM = FuStringUtils.substring(timeStr, 0, 6);
        String day = FuStringUtils.substring(timeStr, 6, 8);
        //随机生成文件名
        String fileName = IdUtil.fastUUID() + "." + suffix;
        return FileUtil.file(FileUtil.getTmpDir(), FuDocConstants.FU_DOC_PATH, moduleName, FuDocConstants.FU_REQUEST_PATH, yyyyMM, day, fileName);
    }

}
