package com.wdf.fudoc.common.constant;

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
}
