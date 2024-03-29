package com.wdf.fudoc.common.constant;

import com.wdf.fudoc.util.FuStringUtils;

import javax.swing.filechooser.FileSystemView;

/**
 * @author wangdingfu
 * @date 2022-11-24 21:48:24
 */
public class PathConstants {

    /**
     * 桌面路径
     */
    public static final String DESKTOP_PATH = FileSystemView.getFileSystemView().getHomeDirectory().getAbsolutePath();

    public static String desktopPath() {
        String property = System.getProperty("os.name");
        if (FuStringUtils.isBlank(property) || property.toLowerCase().startsWith("win")) {
            return DESKTOP_PATH;
        }
        return DESKTOP_PATH + "/Desktop";
    }

}
