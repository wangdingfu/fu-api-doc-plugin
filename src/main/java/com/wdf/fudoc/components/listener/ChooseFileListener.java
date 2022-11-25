package com.wdf.fudoc.components.listener;

import com.intellij.openapi.vfs.VirtualFile;

import java.io.File;

/**
 * 选择文件监听器
 *
 * @author wangdingfu
 * @date 2022-11-25 22:03:09
 */
public interface ChooseFileListener {

    /**
     * 当前文件是否可以选中
     *
     * @param file 鼠标点击的文件或文件夹
     * @return 是否可选中
     */
    boolean isFileSelectable(VirtualFile file);


    /**
     * 选中文件的回调
     *
     * @param file 选中的文件
     */
    void selectedFile(File file);

}
