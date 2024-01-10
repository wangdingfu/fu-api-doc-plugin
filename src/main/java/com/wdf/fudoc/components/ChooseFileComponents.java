package com.wdf.fudoc.components;

import com.intellij.ide.util.PropertiesComponent;
import com.intellij.openapi.fileChooser.FileChooser;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.util.io.FileUtilRt;
import com.intellij.openapi.vfs.VfsUtil;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.wdf.fudoc.components.listener.ChooseFileListener;
import com.wdf.api.util.ProjectUtils;

import java.io.File;

/**
 * 选择文件组件
 *
 * @author wangdingfu
 * @date 2022-11-25 21:03:16
 */
public class ChooseFileComponents {

    /**
     * 业务code 用于标识一笔业务 根据该code缓存上一次选择的目录
     */
    private final String bizCode;

    /**
     * 选择文件弹框标题
     */
    private final String title;

    /**
     * 选择文件弹框提示信息
     */
    private final String message;

    public ChooseFileComponents(String bizCode, String title, String message) {
        this.bizCode = bizCode;
        this.title = title;
        this.message = message;
    }


    /**
     * 弹出选中文件框 当文件被选中后回调业务逻辑
     *
     * @param listener 选择文件监听器
     */
    public void chooseFile(ChooseFileListener listener) {
        FileChooserDescriptor descriptor = new FileChooserDescriptor(false, true, false, false, false, false) {
            @Override
            public boolean isFileSelectable(VirtualFile file) {
                return listener.isFileSelectable(file);
            }
        };
        descriptor.setTitle(title);
        descriptor.setDescription(message);
        String oldPath = getOldPath();
        VirtualFile toSelect = oldPath != null ? VfsUtil.findFileByIoFile(new File(FileUtilRt.toSystemDependentName(oldPath)), false) : null;

        FileChooser.chooseFile(descriptor, ProjectUtils.getCurrProject(), null, toSelect, virtualFile -> {
            File file = VfsUtilCore.virtualToIoFile(virtualFile);
            setOldPath(file);
            //此时选中文件 回调业务逻辑
            listener.selectedFile(file);
        });
    }


    private String getOldPath() {
        return PropertiesComponent.getInstance().getValue(this.bizCode);
    }


    private void setOldPath(File file) {
        String oldPath = file.isDirectory() ? file.getPath() : file.getParent();
        PropertiesComponent.getInstance().setValue(this.bizCode, FileUtilRt.toSystemIndependentName(oldPath));
    }


}
