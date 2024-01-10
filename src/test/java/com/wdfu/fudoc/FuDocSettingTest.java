package com.wdfu.fudoc;

import cn.hutool.core.date.DatePattern;
import cn.hutool.core.io.FileUtil;
import cn.hutool.system.SystemUtil;
import com.wdf.fudoc.common.constant.FuDocConstants;
import com.wdf.fudoc.request.view.ResponseErrorView;
import com.wdf.fudoc.util.FuStringUtils;

import javax.swing.*;
import java.io.File;
import java.util.Date;

/**
 * @author wangdingfu
 * @Description
 * @date 2022-08-06 21:42:27
 */
public class FuDocSettingTest {

    public static void main(String[] args) {
        FileUtil.copyFile(FileUtil.newFile("C:\\Users\\fuge\\Desktop\\斗鱼-工时单-202304-王定福.xls"), FileUtil.newFile("C:\\Users\\fuge\\Desktop\\2.xls"));
    }

    public static File genFilePath(String moduleName) {
        String timeStr = DatePattern.PURE_DATE_FORMAT.format(new Date());
        String yyyyMM = FuStringUtils.substring(timeStr, 0, 6);
        String day = FuStringUtils.substring(timeStr, 6, 8);
        return FileUtil.file(FileUtil.getTmpDir(), FuDocConstants.FU_DOC_PATH, moduleName, yyyyMM, day,"a1.xls");
    }

}
